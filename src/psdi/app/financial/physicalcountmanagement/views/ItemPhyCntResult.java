package psdi.app.financial.physicalcountmanagement.views;

import java.rmi.RemoteException;
import java.util.Vector;

import psdi.app.financial.physicalcountmanagement.phyCntMngLine.itemPhysicalCountLine;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.server.AppService;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class ItemPhyCntResult extends Mbo {

	public ItemPhyCntResult(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MboSetRemote getList(String arg0) throws MXException, RemoteException {
		// TODO Auto-generated method stub
		return super.getList(arg0);
	}

	public void saveItemSet() throws RemoteException, MXException {
		System.out.println("save set fun");
		MboSetRemote mboSet = getThisMboSet();
		Vector mboSetsel = getThisMboSet().getSelection();
		System.out.println("mbo Set = " + mboSet + "and countOfSelection = " + mboSetsel.size());
		System.out.println("count of set = " + mboSet.count());

		MboRemote owner = getOwner();
		String[] params = { owner.getString("STATUS"), "Add " };
		System.out.println("owner = " + owner);
		MboSetRemote linesMboSet = getMboSet("ITEMPHYCNTLINE");
		System.out.println("linesMboSet : " + linesMboSet.getMbo(0) + " and count of relation " + linesMboSet.count());
		if (owner.getString("STATUS").equalsIgnoreCase("WAPPR")) {
			if (mboSetsel.size() == 0) {
				for (int i = 0; i < mboSet.count(); i++) {
					System.out.println("i = " + i);
					MboRemote line1 = linesMboSet.add();
					line1.setValue("itemphycntid", owner.getString("itemphycntid"));
					System.out.println(owner.getString("ITEMPHYCNTNUM"));
					line1.setValue("ITEMPHYCNTNUM", owner.getString("ITEMPHYCNTNUM"));
					line1.setValue("STOREROOM", owner.getString("STOREROOM"));
					line1.setValue("CREATEBY", getUserInfo().getUserName());
					line1.setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					line1.setValue("CONDITIONCODE", mboSet.getMbo(i).getString("CONDITIONCODE"));
					line1.setValue("STOREROOM", mboSet.getMbo(i).getString("LOCATION"));
					line1.setValue("ABCTYPE", mboSet.getMbo(i).getString("ABCTYPE"));
					line1.setValue("BINNUM", mboSet.getMbo(i).getString("BINNUM"));
					line1.setValue("CAPITALIZED", mboSet.getMbo(i).getString("CAPITALIZED"));
					line1.setValue("CATALOGCODE", mboSet.getMbo(i).getString("CATALOGCODE"));
					line1.setValue("COMMODITY", mboSet.getMbo(i).getString("COMMODITYCODE"));
					line1.setValue("COMMODITYGROUP", mboSet.getMbo(i).getString("COMMODITYGROUP"));
					line1.setValue("CONDITIONENABLED", mboSet.getMbo(i).getString("CONDITIONENABLED"));
					line1.setValue("DESCRIPTION", mboSet.getMbo(i).getString("DESCRIPTION"));
					line1.setValue("INVBALANCESID", mboSet.getMbo(i).getString("INVBALANCESID"));
					line1.setValue("ISSUEUNIT", mboSet.getMbo(i).getString("ISSUEUNIT"));
					line1.setValue("ITEMNUM", mboSet.getMbo(i).getString("ITEMNUM"));
					line1.setValue("LOCKED", 1);
					line1.setValue("LOTNUM", mboSet.getMbo(i).getString("LOTNUM"));
					line1.setValue("LOTTYPE", mboSet.getMbo(i).getString("LOTTYPE"));
					line1.setValue("MODELNUM", mboSet.getMbo(i).getString("MODELNUM"));
					line1.setValue("ORDERUNIT", mboSet.getMbo(i).getString("ORDERUNIT"));
					line1.setValue("ORGID", mboSet.getMbo(i).getString("ORGID"));
 					line1.setValue("REORDER", mboSet.getMbo(i).getString("REORDER"));
 					line1.setValue("ROTATING", mboSet.getMbo(i).getString("ROTATING"));
 					line1.setValue("STATUS", mboSet.getMbo(i).getString("STATUS"));
 					line1.setValue("SITEID", mboSet.getMbo(i).getString("SITEID"));
					line1.setValue("oldphycnt", mboSet.getMbo(i).getString("oldphycnt"));
 				}
				System.out.println("selection = 0 ");
			} else {
				System.out.println("count = " + mboSetsel.size());
				System.out.println("mbo of selection = " + mboSetsel.get(0));
				for (int i = 0; i < mboSetsel.size(); i++) {
					ItemPhyCntResult line = (ItemPhyCntResult) mboSetsel.get(i);
					// psdi.app.financial.physicalcountmanagement.phyCntMngLine.itemPhysicalCountLineSet
					System.out.println("line = " + line);
					System.out.println("i = " + i);
					MboRemote line1 = linesMboSet.add();
					line1.setValue("itemphycntid", owner.getString("itemphycntid"));
					line1.setValue("ITEMPHYCNTNUM", owner.getString("ITEMPHYCNTNUM"));
					line1.setValue("CREATEBY", getUserInfo().getUserName());
					line1.setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					line1.setValue("CONDITIONCODE", line.getString("CONDITIONCODE"));
 					line1.setValue("STOREROOM", owner.getString("STOREROOM"));
 					System.out.println("CONDITIONCODE ");
					line1.setValue("ABCTYPE", line.getString("ABCTYPE"));
					line1.setValue("BINNUM", line.getString("BINNUM"));
					line1.setValue("CAPITALIZED", line.getString("CAPITALIZED"));
					line1.setValue("CATALOGCODE", line.getString("CATALOGCODE"));
					line1.setValue("COMMODITY", line.getString("COMMODITYCODE"));
					line1.setValue("COMMODITYGROUP", line.getString("COMMODITYGROUP"));
					line1.setValue("CONDITIONENABLED", line.getString("CONDITIONENABLED"));
					line1.setValue("DESCRIPTION", line.getString("DESCRIPTION"));
					line1.setValue("INVBALANCESID", line.getString("INVBALANCESID"));
					line1.setValue("ISSUEUNIT", line.getString("ISSUEUNIT"));
					line1.setValue("ITEMNUM", line.getString("ITEMNUM"));
					line1.setValue("LOCKED", 1);
					line1.setValue("LOTNUM", line.getString("LOTNUM"));
					line1.setValue("LOTTYPE", line.getString("LOTTYPE"));
					line1.setValue("MODELNUM", line.getString("MODELNUM"));
					line1.setValue("ORDERUNIT", line.getString("ORDERUNIT"));
					line1.setValue("ORGID", line.getString("ORGID"));
					line1.setValue("REORDER", line.getString("REORDER"));
					line1.setValue("ROTATING", line.getString("ROTATING"));
					line1.setValue("STATUS", line.getString("STATUS"));
					line1.setValue("SITEID", line.getString("SITEID"));
					System.out.println("added ");
					super.save();
				}
				super.save();
			}
			super.save();
		} else
			throw new MXApplicationException("filtering", "deleteitem", params);

	}

}
