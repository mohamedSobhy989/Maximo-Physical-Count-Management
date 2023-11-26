package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class AssetSelectionFilterTableBean extends DataBean {

	public AssetSelectionFilterTableBean() {
		super();
	}

	@Override
	protected void initialize() throws MXException, RemoteException {
		System.out.println("init");

		super.initialize();
	}

	@Override
	public int addrow() throws MXException {
		System.out.println("add row");
		return super.addrow();
	}

	@Override
	public MboSetRemote getMboSet() throws MXException, RemoteException {
		System.out.println("get mbo Set ");
		MboRemote mbo = app.getAppBean().getMbo();

		return super.getMboSet();
	}

}
