package com.dstz.base.common.constats;

/**
 * AB module REST constants
 *
 */
public class AbAppRestConstant {

    private AbAppRestConstant() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    /**
     * ORG service prefix (current basic microservice)
     */
    public static final String ORG_SERVICE_PREFIX = "${ab.org-rest-prefix:/ab-org}";

    /**
     * BPM service prefix (current basic microservice)
     */
    public static final String BPM_SERVICE_PREFIX = "${ab.bpm-rest-prefix:/ab-bpm}";
    
    /**
     * DEMO service prefix (currently attached to the bpm microservice)
     */
    public static final String DEMO_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/demo";

    /**
     * AUTH service prefix (currently attached to the org microservice)
     */
    public static final String AUTH_SERVICE_PREFIX = ORG_SERVICE_PREFIX + "/auth";

    /**
     * CMS CMS prefix (currently attached to the bpm microservice)
     */
    public static final String CMS_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/cms";
    
    /**
     * BIZ prefix (currently attached to the bpm microservice)
     */
    public static final String BIZ_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/biz";
    
    /**
     * SYS prefix (currently attached to the bpm microservice)
     */
    public static final String SYS_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/sys";

    /**
     * Salary management salary prefix (currently attached to the bpm microservice)
     */
    public static final String SALARY_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/salary";

    /**
     * Appstore prefix of the application market (currently attached to the bpm microservice)
     */
    public static final String APPSTORE_SERVICE_PREFIX = BPM_SERVICE_PREFIX + "/appstore";
}
