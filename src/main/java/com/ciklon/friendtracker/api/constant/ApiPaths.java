package com.ciklon.friendtracker.api.constant;


public class ApiPaths {
    public static final String LOGIN = "/api/v1/user/login";
    public static final String REGISTER = "/api/v1/user/register";
    public static final String LOGOUT = "/api/v1/user/logout";
    public static final String REFRESH = "/api/v1/user/refresh";
    public static final String ACCESS = "/api/v1/user/access";

    public static final String CONTACT = "/api/v1/contact";
    public static final String CONTACT_LIST = "/api/v1/contact/list";
    public static final String CONTACT_BY_ID = "/api/v1/contact/{id}";

    public static final String FORM = "/api/v1/form";
    public static final String FORM_BY_ID = "/api/v1/form/{id}";
    public static final String CONTACT_FORM_BY_ID = "/api/v1/form/{id}/contact";

    public static final String RATING = "/api/v1/rating";
    public static final String RATING_BY_CONTACT_ID = "/api/v1/rating/contact/{id}";
    public static final String AVERAGE_RATING = "/api/v1/rating/average";

    public static final String QUESTION = "/api/v1/question";
    public static final String QUESTION_BY_ID = "/api/v1/question/{id}";
    public static final String QUESTION_ANSWERS = "/api/v1/question/{id}/answers";

    public static final String USER_ANSWERS_BY_CONTACT = "/api/v1/answers/contact/{id}";
    public static final String QUESTION_ANSWER_BY_ID = "/api/v1/answers/{id}";
    public static final String ALL_QUESTION_ANSWERS_BY_QUESTION_ID = "/api/v1/answers/question/{id}";
    public static final String USER_ANSWERS = "/api/v1/answers";
    public static final String USER_ANSWER = "/api/v1/answers/user";
    public static final String USER_ANSWER_BY_ID = "/api/v1/answers/{id}/user";
    public static final String QUESTIONS_BY_CONTACT_ID = "/api/v1/questions/contact/{id}";
}
