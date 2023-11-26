package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;

import com.ibm.tivoli.maximo.asset.ilog.controls.ILogControl;

import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;
import psdi.webclient.system.controller.ControlInstance;
import psdi.webclient.system.controller.PageInstance;

public class TestFiltering extends DataBean {

	
	@Override
	protected void initialize() throws MXException, RemoteException {
		System.out.println("init in adjust bean class");
		super.initialize();
	}
	@Override
	public synchronized void save() throws MXException {
		System.out.println("save in adjust in bean class");
		super.save();
	}
}
