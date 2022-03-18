package com.git.blog.commmon.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
@Getter
public enum PFStageEnum {
    //开发中、开发完成，申请冒烟、冒烟完成，申请测试、测试完成、集成测试开始、集成测试通过、发布开始、发布通过
    IN_DEVELOPMENT(0,"开发中"),
    DEVELOPMENT_COMPLETED(10,"开发完成，申请冒烟"),
    //SMOKE_TEST_IN_PROGRESS(20,"冒烟测试中"),
    SMOKE_TEST_PASS(30,"冒烟完成，申请测试"),
    //IN_TEST(40,"测试中"),
    TEST_PASS(50,"测试完成"),
    IN_INTEGRATE_TEST(60,"集成测试开始"),
    IN_INTEGRATE_TEST_PASS(70,"集成测试通过"),
    IN_DEPLOYING(80,"发布开始"),
    DEPLOYED(90,"发布通过");
    //IN_BLOCKING(100,"阻塞");


    private Integer value;
    private String desc;

    PFStageEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 进去校验,通过true，否则false
     * @param stage
     * @param progress
     * @return boolean
     */
    public static boolean check(Integer stage, Integer progress) {
        if(stage==null || progress == null || progress<0 || progress>100){
            return false;
        }
        return Arrays.stream(PFStageEnum.values()).anyMatch(i->i.getValue().equals(stage));
    }

    public static PFStageEnum getByValue(Integer stage) {
        if(stage==null){
            return IN_DEVELOPMENT;
        }
        return Arrays.stream(PFStageEnum.values()).filter(i->i.getValue().equals(stage)).findFirst().orElse(IN_DEVELOPMENT);
    }

    /**
     * 判断当前状态是不是dev阶段
     * @param stage
     * @return
     */
    public static boolean isInDev(Integer stage) {
        return IN_DEVELOPMENT.getValue().equals(stage) || DEVELOPMENT_COMPLETED.getValue().equals(stage);
    }

    @Override
    public String toString() {
        return "PFStageEnum{" +
                "value=" + value +
                ", desc='" + desc + '\'' +
                '}';
    }
}
