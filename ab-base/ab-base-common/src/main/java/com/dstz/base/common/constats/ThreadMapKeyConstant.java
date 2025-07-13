package com.dstz.base.common.constats;

import java.util.List;

import cn.hutool.core.collection.CollUtil;

/**
 * Thread variable KEY constant definition
 *
 */
public abstract class ThreadMapKeyConstant {

	public static class DataPrivilege {

		public static final String PREFIX = "DataPrivilege";

		/**
		 * Current user data permissions
		 */
		public static final String CURRENT_USER = PREFIX + ".currentUser";
	}

	public static class QueryFilter {

		/**
		 *
		 */
		public static final String PREFIX = "QueryFilter";

		/**
		 * Allow parameter filtering
		 */
		public static final String ACCESS_QUERY_FILTERS = PREFIX + ".accessQueryFilters";
	}

	public static class BizFormDesign {
		public static final String PREFIX = "BizFormDesign";

		public static final String IMP_CODES = PREFIX + ".impCodes";
	}

	public static class BizData {
		public static final String PREFIX = "BizData";

		public static final String IS_FLOW = PREFIX + ".isFlow";

	}

	public static class BpmFormDataService {
		public static final String PREFIX = "BpmFormDataService";

		public static final String VAR_MAP = PREFIX + ".varMap";

		public static final String BIZ_DATA_MAP = PREFIX + ".bizDataMap";
	}

	public static class DataFormatTools {
		public static final String PREFIX = "DataFormatTools";

		public static final String ERR_MSG_MAP = PREFIX + ".errMsgMap";
	}

	public static class CtrlParserExport {
		public static final String PREFIX = "CtrlParserExport";

		public static final String DIALOG_KEY_SET = PREFIX + ".dialogKeySet";
	}

	public static class FlowImgStream {
		public static final String PREFIX = "FlowImgStream";

		public static final String NODE_MAP = PREFIX + ".nodeMap";
		public static final String FLOW_MAP = PREFIX + ".flowMap";
		public static final String GATE_MAP = PREFIX + ".gateMap";
	}

	public static class DesignQuery {
		public static final String PREFIX = "DesignQuery";

		public static final String MAPPING = PREFIX + ".mapping";

		public static final String INDEX = PREFIX + ".index";
	}

	/**
	 * Application installation, installation related information,
	 * Application list key, form key, process key, custom dialog box, data dictionary, group form, etc.
	 *
	 */
	public static class AppInstallInfo {
		public static final String PREFIX = "AppInfo";

		/**
		 * Customize Dialog Box
		 */
		public static final String DIALOG_KEY = PREFIX + ".dialogKey";
		/**
		 * Custom Lists
		 */
		public static final String GRID_KEY = PREFIX + ".gridKey";
		/**
		 * Form
		 */
		public static final String FORM_KEY = PREFIX + ".formKey";
		public static final String FORM_ID = PREFIX + ".formId";
		/**
		 * Business Entity
		 */
		public static final String TABLE_KEY = PREFIX + ".tableKey";
		/**
		 * Business Objects
		 */
		public static final String BO_KEY = PREFIX + ".boKey";
		/**
		 * process
		 */
		public static final String BPM_DEF_KEY = PREFIX + ".bpmDefKey";
		public static final String BPM_DEF_ID = PREFIX + ".bpmDefId";
		/**
		 * Data dictionary
		 */
		public static final String DICT_KEY = PREFIX + ".dictKey";
		/**
		 * Combination form
		 */
		public static final String FORM_COMBINATION_KEY = PREFIX + ".formCombinationKey";

		/**
		 * Serial number
		 */
		public static final String SERIAL_NO_KEY = PREFIX + ".serialNoKey";

		public static final String MENU_KEY = PREFIX + ".menuKey";

		public static final List<String> APP_INFO_KEY_LIST = CollUtil.toList(DIALOG_KEY, GRID_KEY, FORM_KEY, TABLE_KEY,
				BO_KEY, BPM_DEF_KEY, DICT_KEY, FORM_COMBINATION_KEY, SERIAL_NO_KEY, MENU_KEY);

	}

	/**
	 * Process Start Test Variables
	 */
	public static class FlowStartTest {

		/**
		 * Process startup test
		 */
		public static final String START_TEST = "flowStartTest";

	}

}
