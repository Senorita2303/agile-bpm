package com.dstz.org.core.constant;

import com.dstz.base.common.codes.IBaseCode;

public enum OrgStatusCode implements IBaseCode {

    FROM_MODIFICATION_DEMO_PWD("fromModificationDemoPassword", "In order to prevent malicious destruction of demonstration data, password modification is prohibited!<br/>Your visit information has been counted by us!"),
    USER_DOES_NOT_EXIST("userDoesNotExist", "User does not exist!"),
    OLD_PWD_INPUT_ERROR("oldPwdInputError", "The original password was entered incorrectly!"),
    EMAIL_DOES_NOT_EXIST("emailDoesNotExistPleaseContactAdminResetPwd", "The email address does not exist, please contact the administrator to reset the password"),
    EMAIL_INPUT_ERROR("emailInputErrorPleaseEnterCorrectEmail", "The email address you entered is incorrect. Please enter a correct email address."),
    NEW_PWD_IS_DIFFERENT_CONFIRM_PWD("newPasswordIsDifferentFromConfirmPassword", "New password and confirm password are different"),
    VERIFICATION_CODE_IS_EXPIRED(" verificationCodeIsExpired", "The verification code has expired, please get it again"),
    PWD_RESET_FUNCTION_IS_NOT_ENABLED("PasswordResetFunctionIsNotEnabled ", "The password reset function is not enabled yet, please contact the administrator to reset the password!"),
    ROLE_CODE_IS_EXIST("roleCodeIsExistInTheSystem", "The role code already exists in the system!"),
    GROUP_CODE_IS_EXIST("groupCodeIsExistInTheSystem", "The organization code already exists in the system!"),
    ACCOUNT_IS_EXIST("accountIsExist", "The account already exists, please modify it!"),
    INPUT_INFORMATION_IS_EMPTY("inputInformationIsEmpty", "Input error, please reset your user information"),
    DEL_FAILED_PARAM_IS_EMPTY("deleteFailedParamIsEmpty", "Deletion failed, parameter cannot be empty"),
    OPERATION_FAILURE("operationFailure", "Operation failed, parameter abnormality"),
    IS_SUPER_ADMIN("isSuperAdmin","Only the super administrator can reset the password. Please confirm the current operating user"),
    NOT_ALLOW_DELETE_ADMIN("notAllowDeleteAdmin","Failed to delete user, cannot delete system administrator"),
    NOT_FIND_USER_BY_OPENID("not_find_user_by_openid", "The user has not yet bound a third-party login!"),

    /**
     * Relationship Check
     */
    ORG_RELATION_REMOVE_CHECK("OrgRelationRemoveCheck", "{}");

    private final String code;
    private final String message;

    OrgStatusCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
