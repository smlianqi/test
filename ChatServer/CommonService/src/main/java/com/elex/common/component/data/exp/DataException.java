package com.elex.common.component.data.exp;

/**
 * 数据异常
 * 
 * @author mausmars
 *
 */
public class DataException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private DateErrorType type = DateErrorType.DateError_Defaul;

	public DataException(DateErrorType type) {
		this.type = type;
	}

	public DataException(String str, DateErrorType type) {
		super(str);
		this.type = type;
	}

	public DataException(Throwable e) {
		super(e);
	}

	public DataException(Throwable e, DateErrorType type) {
		super(e);
		this.type = type;
	}

	public DataException(String str, Throwable e, DateErrorType type) {
		super(str, e);
		this.type = type;
	}

	public DateErrorType getType() {
		return type;
	}
}
