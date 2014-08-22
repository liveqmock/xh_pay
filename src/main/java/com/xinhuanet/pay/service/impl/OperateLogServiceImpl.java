package com.xinhuanet.pay.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.dao.OperateLogDao;
import com.xinhuanet.pay.po.OperateLog;
import com.xinhuanet.pay.service.OperateLogService;

@Service
public class OperateLogServiceImpl implements OperateLogService {

	private @Autowired OperateLogDao operaLogDao;
	
	@Override
	public int addOperateLog(OperateLog operaLog) {
		operaLog.setUuid(UUID.randomUUID().toString());
		return operaLogDao.addOperateLog(operaLog);
	}

}
