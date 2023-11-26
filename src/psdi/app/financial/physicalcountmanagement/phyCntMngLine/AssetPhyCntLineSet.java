package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.util.MXException;

public class AssetPhyCntLineSet extends MboSet {

	public AssetPhyCntLineSet(MboServerInterface ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Mbo getMboInstance(MboSet arg0) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return new AssetPhyCntLine(arg0);
	}

}
