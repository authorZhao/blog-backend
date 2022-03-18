package com.git.blog.dto.deploy;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 部署对象
 * @author authorZhao
 * @since 2021-03-12
 */
@Data
@Accessors(chain = true)
public class DeployLoginDTO {

   private String username;
   private String password;
}
