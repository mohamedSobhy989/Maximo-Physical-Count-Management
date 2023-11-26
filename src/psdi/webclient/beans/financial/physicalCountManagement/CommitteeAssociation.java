package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;
 

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class CommitteeAssociation extends DataBean{

	@Override
	protected void initialize() throws MXException, RemoteException {
		System.out.println("initialize");
		super.initialize();
	}
	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute function");
		MboRemote owner = getMbo().getOwner();
		MboRemote mbo = getMbo();
		System.out.println("owner : " + owner);
		System.out.println("mbo : " + mbo);
		
		String status = owner.getString("STATUS");
		System.out.println(status);
		if (status.equals("WAPPR")) {
			String committeeid = mbo.getString("committeeid");
			System.out.println(committeeid);
			//owner.setFieldFlag("committeeid", READONLY, false);
			
			MboSetRemote mboSetUser = mbo.getMboSet("USERCOMMITTEE");
			System.out.println("count of user = " + mboSetUser.count());
			if(mboSetUser.count() == 0)
				throw new MXApplicationException("phycnt", "NoUserAssociated");
			else {
				owner.setValue("committeeid", committeeid);
				return super.execute();
			}
		}else
			throw new MXApplicationException("phycnt","objectreadonly");
		 
	}
	
}
