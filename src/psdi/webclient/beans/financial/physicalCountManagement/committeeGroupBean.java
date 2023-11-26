package psdi.webclient.beans.financial.physicalCountManagement;
import java.rmi.RemoteException;

import psdi.app.financial.physicalcountmanagement.itemPhysicalCount;
import psdi.mbo.MboRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class committeeGroupBean extends DataBean {

	@Override
	protected void initialize() throws MXException, RemoteException {

		System.out.println("initialize bean");
		super.initialize();
	}
	

	

}
