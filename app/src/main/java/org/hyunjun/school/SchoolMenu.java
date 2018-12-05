package org.hyunjun.school;

/**
 * School API
 * 전국 교육청 소속 교육기관의 학사일정, 메뉴를 간단히 불러올 수 있습니다.
 *
 * @author HyunJun Kim
 * @version 3.0
 */
public class SchoolMenu {
    
    public enum Type {
        SIMPLE(0),
        PRECISE(1);
        
        private int type;
        Type(int type) {
            this.type = type;
        }
    }
    
    public Type type;
    
    /**
     * 조식
     */
    public String breakfast;

    /**
     * 중식
     */
    public String lunch;

    /**
     * 석식
     */
    public String dinner;

    public SchoolMenu() {
        breakfast = lunch = dinner = "등록된 식단 정보가 없습니다.";
        type = Type.SIMPLE;
    }

    @Override
    public String toString() {
        if(type == Type.SIMPLE) {
            return "[아침]\n" + breakfast + "\n" + "[점심]\n" + lunch + "\n" + "[저녁]\n" + dinner;
        } else if(type == Type.PRECISE) {
            return "[아침]\n" + breakfast.split("#")[0] + "\n" + "[점심]\n" + lunch.split("#")[0] + "\n" + "[저녁]\n" + dinner.split("#")[0];
        } else {
            return "에러가 발생하였습니다.";
        }
    }
}