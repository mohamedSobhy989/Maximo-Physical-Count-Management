package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;

import psdi.app.financial.physicalcountmanagement.assetPhysicalCount;
import psdi.mbo.MboRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class AssetPhyCntApplying extends DataBean {

	public AssetPhyCntApplying() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() throws MXException, RemoteException {
		System.out.println("initialize");

	}

	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute fun .. apply new physicalcount ");
		MboRemote owner = getMbo().getOwner();
		System.out.println("owner " + owner);
		((assetPhysicalCount) owner).applyPhyCnt();
		super.execute();
		super.save();
		return 1;
	}

}
