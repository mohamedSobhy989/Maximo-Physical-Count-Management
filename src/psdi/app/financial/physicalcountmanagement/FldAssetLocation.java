package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldAssetLocation extends MboValueAdapter {

	public FldAssetLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException {

		MboRemote mbo = getMboValue().getMbo();
		System.out.println("mbo in fldlocation = " + mbo);
		MboSetRemote mboSet = mbo.getMboSet("LINES");
		System.out.println("count of lines = " + mboSet.count());
		String status = mbo.getString("STATUS");
		if (status.equalsIgnoreCase("APPR") || status.equalsIgnoreCase("CAN") || status.equalsIgnoreCase("CLOSE")) {
			mbo.setFieldFlag("location", READONLY, true);
		} else {
			if (mboSet.count() > 0)
				mbo.setFieldFlag("location", READONLY, true);
			else
				mbo.setFieldFlag("location", READONLY, false);
		}

		super.init();
	}

	@Override
	public void action() throws MXException, RemoteException {
		System.out.println("action");
		super.action();
	}

}
