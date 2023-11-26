package psdi.app.financial.physicalcountmanagement.phyCntMngLine;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.util.MXException;

public class FldAssetFount extends MboValueAdapter {

	public FldAssetFount() {
		// TODO Auto-generated constructor stub
	}

	public FldAssetFount(MboValue mbv) {
		super(mbv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException, RemoteException {
		System.out.println("init in fld class phycnt");
		System.out.println("init in :" + getMboValue());
		System.out.println("init in fld  " + getMboValue().getMbo());
		System.out.println("init in fld class  " + getMboValue().getMbo().getOwner());

		MboRemote owner = getMboValue().getMbo().getOwner();
		MboRemote mbo = getMboValue().getMbo();
		System.out.println("mbo in fld new physical count= " + owner);
		System.out.println("mbo STATUS = " + owner.getString("status"));

		if (owner.getString("status").equals("APPR")) {
			mbo.setFieldFlag("ISFOUND", READONLY, false);
		} else
			mbo.setFieldFlag("ISFOUND", READONLY, true);

		super.init();
	}

	@Override
	public void action() throws MXException, RemoteException {
		System.out.println("action method------");
		super.action();
	}

}
