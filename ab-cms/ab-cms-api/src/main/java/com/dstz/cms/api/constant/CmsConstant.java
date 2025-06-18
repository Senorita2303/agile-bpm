package com.dstz.cms.api.constant;


/**
 * @description Constants used in the CMS information module
 */
public class CmsConstant {

    
    /**
     * Release status: (unreleased)
     */
    public static int STATUS_UNPUBLISHED = 0;

    /**
     * Release status: (released)
     */
    public static int STATUS_PUBLISHED = 1;
    
    /**
     * Release status: (offline)
     */
    public static int STATUS_DOWN = 2;
    
    /**
     * Read status (unread)
     */
    public static int TYPE_UN_READ = 0;
    
    /**
     * Read status (read)
     */
    public static int TYPE_IS_READ = 1;
    
    /**
     * Read Status (Expired)
     */
    public static int TYPE_EXPIRED = 2;
    
    /**
     * Query conditions (title fuzzy query)
     */
    public static String TITLE$VLK = "title$VLK";
    
    /**
     * Query conditions (status precise query)
     */
    public static String STATUS$VEQ = "status$VEQ";
    
    /**
     * A string representing the number of comments attribute of a news or announcement
     */
    public static String COMMENTS_NUM = "commentsNum";
    
    /**
     * Comment type (0 announcement, 1 news)
     */
    public static int COMMENT_TYPE_NOTIFY = 0;
    
    /**
     * Comment type (0 announcement, 1 news)
     */
    public static int COMMENT_TYPE_NEWS = 1;
    
    /**
     * The key-content of the data map of the in-site message
     */
    public static String CONTENT = "content";
    
    /**
     * The key-title of the data map of the in-site message
     */
    public static String SUBJECT = "subject";
    
    /**
     * Is it everyone's key?
     */
    public static String ALL = "all";
    
    /**
     * User's key
     */
    public static String USER = "user";
    
    /**
     * Encoded key
     */
    public static String CODE = "code";
    
    /**
     * Role key
     */
    public static String ROLE = "role";
    
    /**
     *    Position key
     */
    public static String POST = "post";
    
    /**
     * Organization key
     */
    public static String ORG = "org";

    /**
     * Internal message push type (two types: 0 notification, 1 to-do)
     */
    public static int INNER_NOTICE = 0;

    /**
     * Internal message push type (two types: 0 notification, 1 to-do)
     */
    public static int INNER_TODO = 1;

    /**
     * Knowledge Base My Document Type (Collection, My, Borrowing, Borrowed)
     */
    public static String DOCUMENT_FAVORITE  = "favorite";

    /**
     * Knowledge Base My Document Type (Collection, My, Borrowing, Borrowed)
     */
    public static String DOCUMENT_MY  = "my";

    /**
     * Knowledge Base My Document Type (Collection, My, Borrowing, Borrowed)
     */
    public static String DOCUMENT_BORROW  = "borrow";

    /**
     * Knowledge Base My Document Type (Collection, My, Borrowing, Borrowed)
     */
    public static String DOCUMENT_PASS  = "pass";


    /**
     * Knowledge Base My Document Type (Denied)
     */
    public static String DOCUMENT_REJECT  = "reject";

    /**
     * Knowledge Base My Document Type (Returned)
     */
    public static String DOCUMENT_REVERT  = "revert";

    /**
     * Knowledge Base My Document Type (No Permissions)
     */
    public static String DOCUMENT_NONE  = "none";

    /**
     * Knowledge Base My Document Source String
     */
    public static String DOCUMENT_SOURCE  = "source";
    /**
     * The root directory ID of the knowledge base
     */
    public static String ROOT_ID  = "0";

    /**
     * Knowledge Base Type Catalog
     */
    public static String FIELD  = "field";
    /**
     * Knowledge Base Type Documentation
     */
    public static String DOC  = "doc";

    /**
     * The borrowing of knowledge base is the data type of collection
     */
    public static Integer FAVORITE_TYPE  = 1;

    /**
     * Knowledge Base Data int type bool: has value/true
     */
    public static String INT_TRUE  =  "1";

    /**
     * Knowledge Base Data int type bool: empty/false
     */
    public static String INT_FALSE =  "0";

    /**
     * Announcement/News title template string
     */
    public static String TITLE =  "${title}";

    /**
     * News template code
     */
    public static String NEWS_MESSAGE_TEMPLATE =  "news_message_template";

    /**
     * Announcement template code
     */
    public static String NOTIFY_MESSAGE_TEMPLATE =  "notify_message_template";
}
