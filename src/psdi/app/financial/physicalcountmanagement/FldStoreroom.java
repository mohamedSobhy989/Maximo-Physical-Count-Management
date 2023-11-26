package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;
 
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldStoreroom extends MboValueAdapter{

	public FldStoreroom() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FldStoreroom(MboValue mbv) {
		super(mbv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException {

		MboRemote mbo = getMboValue().getMbo();
		//policyConflict pcMbo = (policyConflict)getMboValue().getMbo();
		System.out.println("mbo in fldstoreroom = "+mbo   );
		MboSetRemote mboSet = mbo.getMboSet("LINES");
		System.out.println("count of lines = " + mboSet.count());
		String status = mbo.getString("STATUS");
		if(status.equalsIgnoreCase("APPR") || status.equalsIgnoreCase("CAN") || status.equalsIgnoreCase("CLOSE")) {
			mbo.setFieldFlag("STOREROOM", READONLY, true);
		}else {
			if(mboSet.count()>0)
				mbo.setFieldFlag("STOREROOM", READONLY, true);
			else
				mbo.setFieldFlag("STOREROOM", READONLY, false);
		}
		 
		super.init();
	}
	
	@Override
	public void action() throws MXException, RemoteException {
		System.out.println("action");
		super.action();
	}

}
