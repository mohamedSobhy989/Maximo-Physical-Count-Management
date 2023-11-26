package psdi.app.financial.physicalcountmanagement.discrepancies;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.util.MXException;

import java.rmi.RemoteException;

public class itemphycntdiscrepanciesSet extends MboSet {

    public itemphycntdiscrepanciesSet(MboServerInterface ms) throws RemoteException {
        super(ms);
    }

    @Override
    protected Mbo getMboInstance(MboSet mboSet) throws MXException, RemoteException {
        return new itemphycntdiscrepancies(mboSet);
    }
}
