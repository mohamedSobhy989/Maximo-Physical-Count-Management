package psdi.app.financial.physicalcountmanagement.committee;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.util.MXException;

public class PhyCntCommitteeSet extends MboSet {

	public PhyCntCommitteeSet(MboServerInterface ms) throws RemoteException {
		super(ms);
	}

	@Override
	protected Mbo getMboInstance(MboSet arg0) throws MXException, RemoteException {
		return new PhyCntCommittee(arg0);
	}

}
