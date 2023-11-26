package psdi.app.financial.physicalcountmanagement.committee;

import java.rmi.RemoteException;

import psdi.app.financial.physicalcountmanagement.assetPhysicalCount;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class PhyCntCommittee extends Mbo {

	public PhyCntCommittee(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void add() throws MXException, RemoteException {
		System.out.println("add committee group ");
		MboRemote mbo = getOwner();
		System.out.println("mbo------  "+ mbo );
		System.out.println(mbo instanceof assetPhysicalCount);
		if(mbo instanceof assetPhysicalCount ) {
			setValue("COMITEETYPE", "ASSET");
		}else
			setValue("COMITEETYPE", "ITEM");
		
		setFieldFlag("COMMITTEEID", READONLY, false);
		setFieldFlag("DESCRIPTION", READONLY, false);
		super.add();
	}
	@Override
	protected void save() throws MXException, RemoteException {
		System.out.println("save Committe Group ");
		System.out.println(getString("committeeid") + " , description : " + getString("description"));
		super.save();
	}

    public void delete(long accessModifier) throws MXException, RemoteException {
		System.out.println("committee is deleted");
		String committeeID = getString("committeeid");
		System.out.println("committeeid = "  + committeeID);//ITEMPHYCNT
		MboSetRemote itemphycnt = getMboSet("ITEMPHYCNT");
		System.out.println(itemphycnt.count());
		if(itemphycnt.count()==0) {
			super.delete(accessModifier);
			MboSetRemote mboSet = getMboSet("USERCOMMITTEE");
			System.out.println("count of mboSet = " +  mboSet.count());
			for (int i = 0; i < mboSet.count(); i++) {
				MboRemote mbo = mboSet.getMbo(i);
				mbo.delete();
				
			}
			
		}
		else
			throw new MXApplicationException("phycnt" ,"DeleteCommittee");
	}

	public void undelete() throws MXException, RemoteException {
		// TODO Auto-generated method stub
		super.undelete();
		MboSetRemote mboSet = getMboSet("USERCOMMITTEE");
		System.out.println("count of mboSet = " +  mboSet.count());
		for (int i = 0; i < mboSet.count(); i++) {
			MboRemote mbo = mboSet.getMbo(i);
			mbo.undelete();
			
		}
	}
}
