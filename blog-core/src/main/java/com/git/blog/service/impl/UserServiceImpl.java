package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.commmon.enums.StatusEnum;
import com.git.blog.commmon.enums.UserStatusEnum;
import com.git.blog.config.properties.DingProperties;
import com.git.blog.config.properties.SysProperties;
import com.git.blog.config.properties.WxProperties;
import com.git.blog.dao.service.*;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.dto.user.*;
import com.git.blog.dto.wx.WxAccessTokenRspDTO;
import com.git.blog.dto.wx.WxUserInfo;
import com.git.blog.exception.BizException;
import com.git.blog.dto.model.entity.*;
import com.git.blog.service.*;
import com.git.blog.service.bean.AuthBeanMapper;
import com.git.blog.util.JwtUtil;
import com.git.blog.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String WX_LOGIN_CODE = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    /**
     * 个人信息缓存
     */
    private static final String USER_MYSELF_USER_VO = "USER_MYSELF_USER_VO";

    /**
     * 个人菜单树
     */
    private static final String USER_MENU_TREE_BY_ID = "USER_MENU_TREE_BY_ID";

    @Autowired
    private WxProperties wxProperties;
    @Autowired
    private WxOpenService wxOpenService;
    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private UserRoleDaoService userRoleDaoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleDaoService roleDaoService;
    @Autowired
    private AuthBeanMapper authBeanMapper;
    @Autowired
    private SysProperties sysProperties;
    @Autowired
    private RoleMenuDaoService roleMenuDaoService;
    @Autowired
    private MenuDaoService menuDaoService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DingProperties dingProperties;

    @Override
    public WxAccessTokenRspDTO getWxToken(String code) {
        String url =  "https://api.weixin.qq.com/sns/oauth2/access_token";
        url = url + "?appid=" + wxProperties.getWxAppId();
        url = url + "&secret=" + wxProperties.getWxAppSecret();
        url = url + "&code="+ code;
        url = url + "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String result = "";
        if(responseEntity.hasBody()){
            result =  responseEntity.getBody();
        }
        String errorFlag = "errcode";
        if (result!=null && !result.contains(errorFlag)) {
            return JSON.parseObject(result, WxAccessTokenRspDTO.class);
        } else {
            log.error("微信授权返回报文:{}", result);
            return null;
        }
    }

    @Override
    public String getGenCode(String redirectUrl) {
        String url =  String.format(WX_LOGIN_CODE, wxProperties.getWxAppId(),wxProperties.getRedirectLoginPage(),redirectUrl);
        log.info("二维码请求地址：url,{}",url);
        return url;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String loginV2(String code, String state) {
        UserVO userVO = new UserVO();
        //1.获取accToken
        WxAccessTokenRspDTO token = wxOpenService.getAccessToken(wxProperties.getWxAppId(),wxProperties.getWxAppSecret(),code,"authorization_code");

        if(token==null || token.isError()){
            return null;
        }

        //2.登录成功，这个逗号是用于重定向回来的
        User  user = userDaoService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getPassword, token.getOpenid())
                .eq(User::getStatus, StatusEnum.NORMAL.getValue())
        );
        String[] split = state.split(",");
        String host = split[0];
        String redirect = "";
        if(split.length==2){
            redirect = split[1];
        }

        String tokenStr = "";
        if(user != null){
            //可能是一个token
            tokenStr = JwtUtil.createToken(String.valueOf(user.getUid()), "mock", "user", sysProperties.getExpireTime(), sysProperties.getSecretKey());
            return host+"#/auth-redirect?token="+tokenStr+(StringUtils.isNotBlank(redirect)?("&redirect="+redirect):"");
        }

        //3.没有账号自动创建
        WxUserInfo userInfo = wxOpenService.getUserInfo(token.getAccess_token(), token.getOpenid());

        if(userInfo==null || userInfo.isError()){
            return null;
        }
        //新建一个用户
        User user2 = userInfo.convertWxUser();
        user2.setStatus(UserStatusEnum.PENDING_APPROVAL.getValue());
        user2.setUsername(StringUtils.isBlank(user2.getPassword())?StringUtils.EMPTY:user2.getPassword() );

        User wxUser = userDaoService.getByWxOPenId(user2.getPassword());
        //插入时才会新增
        if(wxUser==null) {
            userDaoService.save(user2);
            //绑定默认角色
            List<Role> myself = roleDaoService.list(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, CommonString.ROLE_DEFAULT_MYSELF));
            if(CollectionUtils.isNotEmpty(myself)){
                userRoleDaoService.save(new UserRole().setRoleId(myself.get(0).getRoleId()).setUid(user2.getUid()));
            }

            log.info("save new user：{}",user2);

        }
        tokenStr = JwtUtil.createToken(String.valueOf(wxUser==null?user2.getUid():wxUser.getUid()), "mock", "user", sysProperties.getExpireTime(), sysProperties.getSecretKey());
        //TODO 暂时写死

        return host+"#/auth-redirect?token="+tokenStr+"&redirect="+wxProperties.getRedirectUserPage();

    }

    @Override
    public  List<User> getAdmin() {
        // TODO 后续考虑管理员等直接缓存
        List<Role> adminList = roleService.getAdmin();

        List<UserRole> list = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, adminList.stream().map(Role::getRoleId)
                .collect(Collectors.toList())));
        List<User> users = userDaoService.listByIds(list.stream().map(UserRole::getUid).collect(Collectors.toList()));
        return users;
    }

    @Override
    public Boolean update(UserDTO userDTO) {
        //用户存在与否
        if(userDTO==null || userDTO.getUid()==null){
            return false;
        }

        User byId = userDaoService.getById(userDTO.getUid());
        if(byId==null){
            return false;
        }

        //3.修改用户
        User user = authBeanMapper.convertUserDTOToUser(userDTO);
        user.setUpdateTime(new Date());
        userDaoService.updateById(user);


        //4.新增角色
        List<Long> roleIds = userDTO.getRoleIds();
        if(CollectionUtils.isEmpty(roleIds)){
            return true;
        }
        //2.删除用户角色
        List<UserRole> list = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, byId.getUid()));
        if(CollectionUtils.isNotEmpty(list)){
            userRoleDaoService.removeByIds(list.stream().map(UserRole::getId).collect(Collectors.toList()));
        }


        roleIds = roleIds.stream().distinct().collect(Collectors.toList());
        //权限校验 TODO
        List<Role> roleList = roleDaoService.listByIds(roleIds);

        if(CollectionUtils.isEmpty(roleList)){
            return true;
        }

        List<UserRole> userRoleList = roleList.stream().map(i -> new UserRole().setRoleId(i.getRoleId()).setUid(userDTO.getUid())).collect(Collectors.toList());

        userRoleDaoService.saveBatch(userRoleList);

        return true;
    }

    @Override
    public Boolean approved(Long uid) {
        User byId = userDaoService.getById(uid);
        if(byId==null ||StatusEnum.NORMAL.getValue().equals(byId.getStatus())){
            return false;
        }

        //1.是不是管理员，是不是管理组的
        boolean admin = isAdmin(AuthTheadLocal.get());
        boolean inGroupByName = true;
        if(!admin || !inGroupByName){
            throw new BizException("没有权限审核用户");
        }

        byId.setStatus(StatusEnum.NORMAL.getValue());
        byId.setApprovedUid(AuthTheadLocal.get());
        byId.setUpdateTime(new Date());
        userDaoService.updateById(byId);
        return true;
    }

    @Override
    public UserVO login(LoginDTO loginDTO) {

        //2.登录成功
        User dbUser = userDaoService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getNickname, loginDTO.getNickname())
                .last(CommonString.LAST_SQL_LIMIT_1));

        if(dbUser == null){
            return null;
        }

        String shouldPa = Md5Util.md5(loginDTO.getPassword() + dbUser.getSalt());
        if(!Objects.equals(shouldPa,dbUser.getPassword())){
            return null;
        }

        UserVO userVO = new UserVO();
        //可能是一个token
        userVO.setToken(JwtUtil.createToken(String.valueOf(dbUser.getUid()), "mock", "user", 12, sysProperties.getSecretKey()));
        userVO.setNickname(dbUser.getNickname());
        userVO.setUsername(dbUser.getUsername());
        List<RoleVO> roleVOList = roleService.getRoleListByUid(dbUser.getUid());
        userVO.setRoleList(roleVOList);
        return userVO;
    }

    @Override
    public UserVO loginByNickName(String nickName) {
        UserVO userVO = new UserVO();
        //2.登录成功
        List<User> userList = userDaoService.list(new LambdaQueryWrapper<User>()
                        .eq(User::getPassword, nickName)
        );
        User user = null;
        if(CollectionUtils.isNotEmpty(userList)){
            user = userList.get(0);
        }
        if(user == null){
            return userVO;
        }
        //可能是一个token
        userVO.setToken(JwtUtil.createToken(String.valueOf(user.getUid()), "mock", "user", 2, sysProperties.getSecretKey()));
        userVO.setUsername(user.getUsername());
        List<RoleVO> roleVOList = roleService.getRoleListByUid(user.getUid());
        userVO.setRoleList(roleVOList);
        return userVO;
    }

    @Override
    public UserVO getUserInfo(Long uid) {
        if(uid==null){
            return null;
        }
        User user = userDaoService.getById(uid);
        if(user == null){
            return null;
        }
        //拥有管理员权限，没有

        UserVO userVO = new UserVO();
        userVO.setUid(user.getUid());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getPassword());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        userVO.setIntroduce(user.getIntroduce());
        List<RoleVO> roleVOList = roleService.getRoleListByUid(user.getUid());
        userVO.setRoleList(roleVOList);
        return userVO;
    }


    @Override
    public Boolean delete(Long uid) {
        if(uid==null){
            return null;
        }
        User user = userDaoService.getById(uid);
        if(user == null){
            return null;
        }
        user.setStatus(StatusEnum.DISABLE.getValue());
        user.setUpdateTime(new Date());
        //修改人未记录
        return userDaoService.updateById(user);
    }

    @Override
    public Page page(UserQueryDTO userQueryDTO) {
        Page<User> page = new Page(userQueryDTO.getCurrent(), userQueryDTO.getPageSize());
        page = userDaoService.page(page, new LambdaQueryWrapper<User>()
                .eq(userQueryDTO.getStatus()!=null,User::getStatus,userQueryDTO.getStatus())
                .and(StringUtils.isNotBlank(userQueryDTO.getUsername()),i->
                        i.like(StringUtils.isNotBlank(userQueryDTO.getUsername()), User::getUsername, userQueryDTO.getUsername())
                                .or(StringUtils.isNotBlank(userQueryDTO.getUsername())).like(StringUtils.isNotBlank(userQueryDTO.getUsername()), User::getPassword, userQueryDTO.getUsername())
                )
                .orderByDesc(User::getUpdateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new Page();
        }

        Set<Long> allUid = page.getRecords().stream().map(User::getUid).collect(Collectors.toSet());
        Set<Long> uidSet = page.getRecords().stream().map(User::getApprovedUid).collect(Collectors.toSet());
        List<User> users = userDaoService.listByIds(uidSet);
        Map<Long, User> collect = new HashMap<>();
        if(CollectionUtils.isNotEmpty(users)){
            collect = users.stream().collect(Collectors.toMap(User::getUid, Function.identity()));
        }
        final Map<Long, User> userMap = collect;

        //角色暂时，分开查询
        List roleVOList = page.getRecords().stream().map(i -> {
            UserVO roleVO = new UserVO();
            BeanUtils.copyProperties(i, roleVO);
            roleVO.setAvatar(Optional.ofNullable(userMap.get(i.getUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return roleVO;
        }).collect(Collectors.toList());
        page.setRecords(roleVOList);

        //角色查询
        List<UserRole> list = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().in(UserRole::getUid,allUid));
        if(CollectionUtils.isEmpty(list)){
            return page;
        }
        Map<Long, List<UserRole>> userRoleMap = list.stream().collect(Collectors.groupingBy(UserRole::getUid));


        List<Role> roleList = roleDaoService.listByIds(list.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList()));

        if(CollectionUtils.isEmpty(roleList)){
            return page;
        }
        Map<Long, Role> roleMap = roleList.stream().collect(Collectors.toMap(Role::getRoleId, Function.identity()));

        ((List<UserVO>) roleVOList).forEach(i->{
            List<UserRole> userRoleList = userRoleMap.get(i.getUid());
            if(CollectionUtils.isEmpty(userRoleList)){
                return;
            }
            List<String> roleName = new ArrayList<>();
            userRoleList.forEach(ur->{
                roleName.add(Optional.ofNullable(roleMap.get(ur.getRoleId())).map(Role::getRoleName).orElse(StringUtils.EMPTY));
            });
            String roleNameList = roleName.stream().collect(Collectors.joining(","));
            i.setRoleNameList(roleNameList);
        });


        return page;
    }


    @Override
    public List<MenuTreeVO> getUserMenu(Long uid) {
        List<MenuTreeVO> menuVOList = new ArrayList<>();
        if(uid==null){
            return menuVOList;
        }
        //根节点
        Menu root = menuService.getRootMenu();
        if(root == null){
            return menuVOList;
        }
        List<Menu> menus = getUserAllMenu(uid);
        if(CollectionUtils.isEmpty(menus)){
            return menuVOList;
        }

        //转化为树
        menuVOList = menus.stream().distinct().map(authBeanMapper::convertMenuToMenuTreeVO).sorted(Comparator.comparingLong(MenuTreeVO::getMenuSort)).collect(Collectors.toList());
        MenuTreeVO menuTreeVO = Optional.of(root).map(authBeanMapper::convertMenuToMenuTreeVO).get();
        //没有一级菜单就没有
        menuService.genTreeMap(menuTreeVO,menuVOList);
        return menuTreeVO.getChildren();

    }

    @Override
    public List<Menu> getUserAllMenu(Long uid) {

        List<UserRole> list = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, uid));
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        //角色
        List<Long> roleIds = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleMenu> roleMenuList = roleMenuDaoService.list(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, roleIds));

        if(CollectionUtils.isEmpty(roleMenuList)){
            return new ArrayList<>();
        }

        //菜单
        List<Long> menuIdList = roleMenuList.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        List<Menu> menuList = new ArrayList<>();
        List<Menu> menus = menuDaoService.listByIds(menuIdList);
        if(CollectionUtils.isNotEmpty(menus)){
            menuList.addAll(menus);
        }
        //转化为树
        List<Menu> collect = menuList.stream().distinct().sorted(Comparator.comparingLong(Menu::getMenuSort)).collect(Collectors.toList());
        return collect;

    }

    @Override
    public List<UserVO> select(String roleName) {
        if(StringUtils.isBlank(roleName)){
            return selectAllUser(null);

        }
        //按照角色来的，角色是后端写死的,没有的话返回所有
        Role role = roleDaoService.getByName(roleName);
        if(role == null){
            return new ArrayList<>();
        }
        List<UserRole> userRoleList = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, role.getRoleId()));

        if(CollectionUtils.isEmpty(userRoleList)){
            return new ArrayList<>();
        }
        return selectAllUser(userRoleList.stream().map(UserRole::getUid).collect(Collectors.toList()));

    }

    private List<UserVO> selectAllUser(List<Long> uidList){
        List<User> list = userDaoService.list(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, StatusEnum.NORMAL.getValue())
                .in(CollectionUtils.isNotEmpty(uidList),User::getUid,uidList));
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(i->new UserVO().setUid(i.getUid()).
                setUsername(StringUtils.isBlank(i.getUsername())?i.getPassword():i.getUsername())).collect(Collectors.toList());
    }

    @Override
    public UserMenuVO getUserMySelf(Long uid) {

        UserMenuVO userMenuVO = new UserMenuVO();
        User user = userDaoService.getById(uid);
        if(user==null || StatusEnum.DISABLE.getValue().equals(user.getStatus())){
            log.warn("用户状态不正常，uid={}",uid);
            throw new BizException("当前用户不存在或者被禁用");
        }
        userMenuVO.setUsername(user.getUsername());
        userMenuVO.setIntroduce(user.getIntroduce());
        //角色
        List<UserRole> userRoleList = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, uid));

        if(CollectionUtils.isEmpty(userRoleList)){
            return userMenuVO;
        }

        List<Role> roleList = roleDaoService.listByIds(userRoleList.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList()));

        if(CollectionUtils.isEmpty(roleList)){
            return userMenuVO;
        }

        List<RoleVO> roleVOList = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        roleList.stream().filter(i-> StatusEnum.NORMAL.getValue().equals(i.getStatus())).forEach(i->{
            if(!UserStatusEnum.DISABLE.getValue().equals(user.getStatus())){
                roleVOList.add(new RoleVO().setRoleName(i.getRoleName()));
                roleIds.add(i.getRoleId());
            }
        });

        userMenuVO.setRoleList(roleVOList);

        //菜单,2021-02-26改为树形菜单，
        List<MenuTreeVO> userMenu = getUserMenu(uid);
        userMenuVO.setMenuTreeVOList(userMenu);

        /*List<RoleMenu> roleMenuList = roleMenuDaoService.list(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, roleIds));

        if(CollectionUtils.isEmpty(roleMenuList)){
            return userMenuVO;
        }

        List<Menu> menuList = menuDaoService.listByIds(roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList()));

        if(CollectionUtils.isEmpty(menuList)){
            return userMenuVO;
        }

        List<MenuTreeVO> menuTreeVOList = menuList.stream().filter(i -> StatusEnum.NORMAL.getValue().equals(i.getStatus())).map(authBeanMapper::convertMenuToMenuTreeVO).collect(Collectors.toList());
        userMenuVO.setMenuTreeVOList(menuTreeVOList);*/
        return userMenuVO;
    }


    @Override
    public Map<Long, User> getUserMapByUid(List<Long> uidList) {

        if(CollectionUtils.isEmpty(uidList)){
            return new HashMap<>();
        }
        uidList = uidList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());

        if(CollectionUtils.isEmpty(uidList)){
            return new HashMap<>();
        }

        List<User> users = userDaoService.listByIds(uidList);
        if(CollectionUtils.isEmpty(users)){
            return new HashMap<>();
        }
        return users.stream().filter(i->StatusEnum.NORMAL.getValue().equals(i.getStatus())).collect(Collectors.toMap(User::getUid, Function.identity()));
    }

    @Override
    public User getUserById(Long uid) {
        return userDaoService.getById(uid);
    }

    @Override
    public boolean isAdmin(Long uid) {
        List<User> admin = getAdmin();
        return admin!=null && admin.stream().anyMatch(i->i.getUid().equals(uid));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean UserBatchDTO(UserBatchDTO userBatchDTO) {
        List<Long> uidList = userBatchDTO.getUidList();
        if(CollectionUtils.isEmpty(uidList)){
            return false;
        }
        List<User> users = userDaoService.listByIds(uidList);
        if(users.size() < uidList.size()){
            log.warn("批量修改用户，有不存在的用户id{}",JSON.toJSONString(userBatchDTO));
        }

        List<UserDTO> collect = users.stream().map(i -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUid(i.getUid());
            userDTO.setIntroduce(userBatchDTO.getIntroduce());
            userDTO.setRoleIds(userBatchDTO.getRoleIds());
            if (!i.getStatus().equals(userBatchDTO.getStatus())) {
                userDTO.setApprovedUid(AuthTheadLocal.get());
            }
            userDTO.setStatus(userBatchDTO.getStatus());
            return userDTO;
        }).collect(Collectors.toList());
        collect.forEach(this::update);
        return true;
    }

    public static void main(String[] args) {
        String admin123465 = Md5Util.md5("admin123465");
        String shouldPa = Md5Util.md5(admin123465 + "4534341231321321");
        System.out.println("shouldPa = " + shouldPa);
    }
}
