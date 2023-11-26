package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldNewPhysCnt extends MboValueAdapter {

	public FldNewPhysCnt() {
		// TODO Auto-generated constructor stub
	}

	public FldNewPhysCnt(MboValue mbv) {
		super(mbv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException { 
		
		MboRemote owner = getMboValue().getMbo().getOwner();
		MboRemote mbo = getMboValue().getMbo();
 		System.out.println("mbo in fld new physical count= "+owner   );
 		System.out.println("mbo STATUS = "+owner.getString("status")   );
 		
		if(owner.getString("status").equals("APPR")) {
				mbo.setFieldFlag("NEWPHYSCNT", READONLY, false);      
		}else
			mbo.setFieldFlag("NEWPHYSCNT", READONLY, true);
		
		super.init(); 
	}
	
	
}
