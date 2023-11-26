package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldCommitteeID extends MboValueAdapter{

	public FldCommitteeID() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FldCommitteeID(MboValue mbv) {
		super(mbv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException {

		MboRemote mbo = getMboValue().getMbo();
		//policyConflict pcMbo = (policyConflict)getMboValue().getMbo();
		System.out.println("mbo in fldCOMMITTEE = "+mbo   );
		
		MboSetRemote mboSet = mbo.getMboSet("PERSON");
		System.out.println("count of COMMITTEE = " + mboSet.count());
		if(mbo.getString("STATUS").equals("WAPPR")) {
			if(mboSet.count()>0)
				mbo.setFieldFlag("COMMITTEEID", READONLY, true);
			else
				mbo.setFieldFlag("COMMITTEEID", READONLY, false);
		}else
			mbo.setFieldFlag("COMMITTEEID", READONLY, true);
		
		super.init();
	}
	
	@Override
	public void action() throws MXException, RemoteException {
		System.out.println("mbo in fldcommittee = action"  );
		super.action();
	}

}
