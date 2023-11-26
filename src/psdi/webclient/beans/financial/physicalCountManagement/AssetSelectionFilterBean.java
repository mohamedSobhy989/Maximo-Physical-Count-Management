
package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;
import java.util.Vector;

import psdi.app.financial.physicalcountmanagement.assetPhysicalCount;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.SqlFormat;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class AssetSelectionFilterBean extends DataBean {

	public AssetSelectionFilterBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initialize() throws MXException, RemoteException {

		System.out.println("initialize");

		MboRemote mbo = app.getAppBean().getMbo();
		System.out.println("initialize : mbo = " + mbo);

		String assetnum = mbo.getString("ASSETPHYCNTNUM");
		String location = mbo.getString("location");
		Boolean child = mbo.getBoolean("child");
		System.out.println("assetnum : " + assetnum + " location : " + location + " include child : " + child);

		getCondition(mbo);
		String where;
		MboSetRemote mboSet = mbo.getMboSet("ASSETPHYCNT");
		if (child) {
			where = " location = '" + location + "' and status = 'OPERATING' and  assetnum not in " + listOfAssetNum;
		} else {
			where = " location = '" + location + "' and status = 'OPERATING' and parent is null and assetnum not in "
					+ listOfAssetNum;
		}

		System.out.println("where " + where);
		mboSet.setWhere(where);
		System.out.println(mboSet.getWhere());
		super.initialize();
	}

	private String listOfAssetNum = "";

	private void getCondition(MboRemote mbo) throws RemoteException, MXException {
		String statement = ("locked =1");
		System.out.println("statement : " + statement);
		SqlFormat sqlFormat = new SqlFormat(mbo.getUserInfo(), statement);
		MboSetRemote mboSet = mbo.getMboSet("$ASSETPHYCNTLINE", "ASSETPHYCNTLINE", sqlFormat.format());
		System.out.println("count of mboSet 2: " + mboSet.count());

		listOfAssetNum = "('";
		for (int i = 0; i < mboSet.count(); i++) {
			if (mboSet.count() - i == 1) {
				listOfAssetNum = listOfAssetNum + mboSet.getMbo(i).getString("assetnum") + "'";
			} else
				listOfAssetNum = listOfAssetNum + mboSet.getMbo(i).getString("assetnum") + "','";
		}
		listOfAssetNum = listOfAssetNum + ")";

		System.out.println("listOfAssetNum : " + listOfAssetNum);
	}

	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute");
		MboRemote mbo = getMbo().getOwner();
		System.out.println("mbo = " + mbo);
		String param[] = { "Add" };
		if (mbo.getString("status").equalsIgnoreCase("CAN")) {
			throw new MXApplicationException("Canceled", "assetCancel");
		} else if (mbo.getString("status").equalsIgnoreCase("WAPPR")) {
			MboSetRemote mboSet = getMbo().getThisMboSet();
			Vector mboSetsel = getMbo().getThisMboSet().getSelection();
			System.out.println("mboSet: " + mboSet + " , count = " + mboSet.count());
			System.out.println("assetnum of (0) : " + mboSet.getMbo(0).getString("assetnum") + " and description : "
					+ mboSet.getMbo(0).getString("Description"));
			((assetPhysicalCount) mbo).saveAssetSetLines(mbo, mboSet, mboSetsel);
			return super.execute();
		} else if (mbo.getString("status").equalsIgnoreCase("APPR")) {
			throw new MXApplicationException("Approved", "assetApproved", param);
		}
		return super.execute();
	}

}
