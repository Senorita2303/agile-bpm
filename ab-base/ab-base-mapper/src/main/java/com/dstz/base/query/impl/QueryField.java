package com.dstz.base.query.impl;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.FieldRelation;

public class QueryField {
	/**
	 * Field Name
	 */
	private String fieldName;
	/**
	 * Field Conditions
	 */
	private ConditionType condition;

	private String inValue ;

	/**
	 * Field association default AND
	 */
	private FieldRelation fieldRelation = FieldRelation.AND;


	public QueryField(String fieldName,ConditionType condition,String inValue) {
		this.setFieldName(fieldName);
		this.setCondition(condition);
		this.inValue = inValue;
	}
	// Processing multi-table SQL, for example, the a.xxx parameter is changed to a_xxx
	private String getParamName() {
		return fieldName.replace(CharUtil.DOT, CharUtil.UNDERLINE);
	}

	public Object getSql() {
		if (condition == null) {
			condition = ConditionType.EQUAL;
		}

		String fieldParamName = StrUtil.concat(true, "#{",getParamName(), "}");
		String conName=this.fieldName.contains("-")?this.fieldName.split("-")[0]:this.fieldName;


		switch (condition) {

		// equal
		case EQUAL: 		return StrUtil.concat(false, this.fieldName,	" = ",		fieldParamName);

		/*case EQUAL_IGNORE_CASE: return  "upper(" + this.fieldName + ") = 	" + 		fieldParamName;*/

		case LESS: 			return StrUtil.concat(false, conName,	" < ",		fieldParamName);

		case LESS_EQUAL: 	return StrUtil.concat(false, conName,	" <= ",		fieldParamName);

		case GREAT:			return StrUtil.concat(false, this.fieldName,	" > ",		fieldParamName);

		case GREAT_EQUAL:	return StrUtil.concat(false, this.fieldName,	"  >= ",	fieldParamName);

		case NOT_EQUAL:		return StrUtil.concat(false, this.fieldName,	"  != ",	fieldParamName);

		case LEFT_LIKE:		return StrUtil.concat(false, this.fieldName,	"  like ",	fieldParamName);

		case RIGHT_LIKE:	return StrUtil.concat(false, this.fieldName,	"  like ",	fieldParamName);

		case LIKE:			return StrUtil.concat(false, this.fieldName,	"  like ",	fieldParamName);

		case NOTNULL:		return this.fieldName.concat(" is not null ");

		case IS_NULL:		return this.fieldName.concat(" is null ");

		case IN:			return StrUtil.concat(false, this.fieldName,	" in  ( ",this.inValue ," )");

		case NOT_IN:		return StrUtil.concat(false, this.fieldName,	" not in ( ",this.inValue ," )");

		case BETWEEN:
			// between
			if (fieldName.endsWith("-end")) {
				return  fieldName.substring(0, fieldName.length() - 4) + " <= " + fieldParamName;
			} else {
				fieldName += " >= " + fieldParamName;
			}
		}

		return StrUtil.EMPTY;
	}


	public ConditionType getCondition() {
		return condition;
	}
	public void setCondition(ConditionType condition) {
		this.condition = condition;
	}


	public String getFieldName() {
		return fieldName;
	}


	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	public FieldRelation getFieldRelation() {
		return fieldRelation;
	}


	public void setFieldRelation(FieldRelation fieldRelation) {
		this.fieldRelation = fieldRelation;
	}


	public String getInValue() {
		return inValue;
	}


	public void setInValue(String inValue) {
		this.inValue = inValue;
	}

}
