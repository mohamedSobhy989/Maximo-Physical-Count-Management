package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.util.MXException;

public class itemPhysicalCountLineSet extends MboSet {

	public itemPhysicalCountLineSet(MboServerInterface ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Mbo getMboInstance(MboSet arg0) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return new itemPhysicalCountLine(arg0);
	}

}
