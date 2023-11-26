package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldAssetChild extends MboValueAdapter {

	public FldAssetChild(MboValue mbv) {
		super(mbv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException {
		System.out.println("init in fld class phycnt FldAssetChild");
		System.out.println("init in :" + getMboValue());
		System.out.println("init in fld  " + getMboValue().getMbo());

		MboRemote mbo = getMboValue().getMbo();

		MboSetRemote mboSet = mbo.getMboSet("LINES");
		System.out.println("count : " + mboSet.count());
		if (mboSet.count() == 0) {
			mbo.setFieldFlag("CHILD", READONLY, false);
		} else
			mbo.setFieldFlag("CHILD", READONLY, true);
		System.out.println("done");
		super.init();
	}

	@Override
	public void action() throws MXException, RemoteException {
		System.out.println("action method------");
		super.action();
	}

}
