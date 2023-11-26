package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class AssetPhyCntLine extends Mbo {

	public AssetPhyCntLine(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	public void deleteAssetList() throws RemoteException, MXException
	{
		System.out.println(" delete asset list");
		MboRemote owner = getOwner();
		String status = owner.getString("STATUS"); 
		MboSetRemote mboSet = getThisMboSet();
		System.out.println("count of list is " + mboSet.count());
		if(status.equalsIgnoreCase("WAPPR")) {
			for (int i = 0; i < mboSet.count(); i++) {
				mboSet.getMbo(i).delete();
			}
		}else
			throw new MXApplicationException("clearasset","clearing");
	}
	
	@Override
	public void delete() throws MXException, RemoteException {
		
		System.out.println(" delete asset phy cnt");
		MboRemote owner = getOwner();
		String status = owner.getString("STATUS"); 
		String param[] = {"delete"}; 
		if (status.equalsIgnoreCase("CAN")) {
			throw new MXApplicationException("clearasset","clearing");
		}else if  (status.equalsIgnoreCase("WAPPR")) {
			super.delete();
		}else if  (status.equalsIgnoreCase("APPR")) {
			throw new MXApplicationException("Approved","assetApproved",param) ;
		}
	}
	
}
