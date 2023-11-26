package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

import psdi.app.asset.Asset;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.server.AppService;
import psdi.util.MXException;

public class assetPhysicalCount extends Mbo {

	public assetPhysicalCount(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	public void init() throws MXException {

		setFieldFlag("CREATEDBY", READONLY, true);
		setFieldFlag("CREATEDATE", READONLY, true);
		setFieldFlag("ORGID", READONLY, true);
		setFieldFlag("SITEID", READONLY, true);
		setFieldFlag("STATUS", READONLY, true);
		setFieldFlag("STATUSDATE", READONLY, true);
		super.init();
	}

	@Override
	public void add() throws MXException, RemoteException {
		// TODO Auto-generated method stub

		setFieldFlag("CREATEDBY", READONLY, false);
		setFieldFlag("CREATEDATE", READONLY, false);
		setFieldFlag("ORGID", READONLY, false);
		setFieldFlag("SITEID", READONLY, false);
		setFieldFlag("STATUS", READONLY, false);
		setFieldFlag("STATUSDATE", READONLY, false);

		setValue("CREATEDBY", getUserInfo().getUserName());
		setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
		setValue("ORGID", this.getProfile().getDefaultOrg());
		setValue("SITEID", this.getProfile().getDefaultSite());
		setValue("STATUS", "WAPPR");
		setValue("STATUSDATE", ((AppService) this.getMboServer()).getMXServer().getDate());

		setFieldFlag("CREATEDBY", READONLY, true);
		setFieldFlag("CREATEDATE", READONLY, true);
		setFieldFlag("ORGID", READONLY, true);
		setFieldFlag("SITEID", READONLY, true);
		setFieldFlag("STATUS", READONLY, true);
		setFieldFlag("STATUSDATE", READONLY, true);
		super.add();
	}

	public void changeStatus(String newStatus, Date newStatusDate) throws RemoteException, MXException {
		System.out.println("new status is : " + newStatus + " .. status Date is : " + newStatusDate);
		if (newStatusDate.equals("") || newStatusDate == null) {
			newStatusDate = ((AppService) this.getMboServer()).getMXServer().getDate();
		}
		setFieldFlag("STATUS", READONLY, false);
		setFieldFlag("STATUSDATE", READONLY, false);
		this.setValue("STATUS", newStatus);
		this.setValue("STATUSDATE", newStatusDate);
		setFieldFlag("STATUS", READONLY, true);
		setFieldFlag("STATUSDATE", READONLY, true);
	}

	public void changeAssetLoced() throws RemoteException, MXException {
		System.out.println("ChangeLocked Flag");
		MboSetRemote lines = getMboSet("LINES");
		System.out.println("count ---- > " + lines.count());
		for (int i = 0; i < lines.count(); i++) {
			System.out.println(lines.getMbo(i).getString("locked"));
			lines.getMbo(i).setValue("locked", 0);
			// System.out.println("item " + lines.getMbo(i).getString("itemnum") + "is
			// unlocked");
		}

	}

	public void saveAssetSetLines(MboRemote owner, MboSetRemote mboSet, Vector mboSetsel)
			throws RemoteException, MXException {
		System.out.println("save set fun");
		System.out.println("mbo Set = " + mboSet + "and count = " + mboSet.count());
		System.out.println("mbo Set Selection = " + mboSetsel + "countOfSelection = " + mboSetsel.size());
		System.out.println("owner = " + owner);
		String[] params = { owner.getString("STATUS"), "Add " };
		MboSetRemote linesMboSet = owner.getMboSet("LINES");
		System.out.println("linesMboSet 4: " + linesMboSet.getMbo(0) + " and count of relation " + linesMboSet.count());

		if (owner.getString("STATUS").equalsIgnoreCase("WAPPR")) {
			System.out.println("status : " + owner.getString("status"));
			if (mboSetsel.size() == 0) {
				for (int i = 0; i < mboSet.count(); i++) {
					System.out.println("i = " + i);
					MboRemote line1 = linesMboSet.add();
					line1.setValue("ASSETPHYCNTNUM", owner.getString("ASSETPHYCNTNUM"));
					line1.setValue("LOCATION", owner.getString("LOCATION"));
					line1.setValue("committeeid", owner.getString("committeeid"));
					line1.setValue("CREATEBY", getUserInfo().getUserName());
					line1.setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					line1.setValue("DESCRIPTION", mboSet.getMbo(i).getString("DESCRIPTION"));
					line1.setValue("ASSETNUM", mboSet.getMbo(i).getString("ASSETNUM"));
					line1.setValue("ASSETTYPE", mboSet.getMbo(i).getString("ASSETTYPE"));
					line1.setValue("LABELNUM", mboSet.getMbo(i).getString("LABELNUM"));
					line1.setValue("PLUSCASSETDEPT", mboSet.getMbo(i).getString("PLUSCASSETDEPT"));
					line1.setValue("LOCKED", 1);
					line1.setValue("ORGID", mboSet.getMbo(i).getString("ORGID"));
					line1.setValue("STATUS", "OPERATING");
					line1.setValue("SITEID", mboSet.getMbo(i).getString("siteid"));
				}
				System.out.println("selection = 0 ");
			} else {
				System.out.println("count = " + mboSetsel.size());
				System.out.println("mbo of selection = " + mboSetsel.get(0));
				for (int i = 0; i < mboSetsel.size(); i++) {
					Asset line = (Asset) mboSetsel.get(i);
					// psdi.app.financial.physicalcountmanagement.phyCntMngLine.itemPhysicalCountLineSet
					System.out.println("line = " + line);
					System.out.println("i = " + i);
					MboRemote line1 = linesMboSet.add();
					line1.setValue("ASSETPHYCNTNUM", owner.getString("ASSETPHYCNTNUM"));
					line1.setValue("LOCATION", owner.getString("LOCATION"));
					line1.setValue("committeeid", owner.getString("committeeid"));
					line1.setValue("CREATEBY", getUserInfo().getUserName());
					line1.setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					line1.setValue("ASSETNUM", line.getString("ASSETNUM"));
					line1.setValue("ASSETTYPE", line.getString("ASSETTYPE"));
					line1.setValue("LABELNUM", line.getString("LABELNUM"));
					line1.setValue("PLUSCASSETDEPT", line.getString("PLUSCASSETDEPT"));

					line1.setValue("DESCRIPTION", line.getString("DESCRIPTION"));
					line1.setValue("LOCKED", 1);
					line1.setValue("ORGID", line.getString("ORGID"));
					line1.setValue("STATUS", "OPERATING");
					line1.setValue("SITEID", line.getString("siteid"));
					System.out.println("added ");
					super.save();
				}
				super.save();
			}
			super.save();
		}
	}

	public void applyPhyCnt() throws RemoteException, MXException {
		System.out.println("applyPhyCnt..........");
		String siteid = getString("siteid");
		System.out.println("siteid " + siteid);
		String assetphycntnum = getString("ASSETPHYCNTNUM");
		System.out.println("assetphycntnum " + assetphycntnum);
		String committeeid = getString("committeeid");
		System.out.println("committeeid " + committeeid);
		String location = getString("location");
		System.out.println(" location " + location);

		MboSetRemote lines = this.getMboSet("PHYCNTAPPLYING");// itemphycntline
		System.out.println("count of lines = " + lines.count());
		MboSetRemote mboSet = this.getMboSet("assets");
		int count = 0;
		int i = 0;
		if (getString("status").equals("APPR")) {
			for (i = 0; i < mboSet.count(); i++) {

				String assetLine = lines.getMbo(count).getString("assetnum");
				String assets = mboSet.getMbo(i).getString("assetnum");

				if (assetLine.equals(assets)) {
					MboRemote assetMbo = mboSet.getMbo(i);
					MboRemote assetLineMbo = lines.getMbo(count);
					System.out.println("asset = " + assetMbo + " ---> and assetline : " + assetLineMbo);
					System.out.println("assetnum  = " + assetMbo.getString("assetnum") + " ---> and line assetnum : "
							+ assetLineMbo.getString("assetnum"));
					System.out.println("count = " + count + " ---> and lines count : " + lines.count()
							+ "mboset count --->" + mboSet.count());
					assetMbo.setFieldFlag("ISFOUND", READONLY, false);
					assetMbo.setFieldFlag("PHYCNTDATE", READONLY, false);
					assetMbo.setFieldFlag("ASSETPHYCNTNUM", READONLY, false);
					assetMbo.setFieldFlag("COMMITTEEID", READONLY, false);
					assetMbo.setValue("ISFOUND", assetLineMbo.getBoolean("isfound"));
					System.out.println("is found added");
					if (assetLineMbo.getDate("PHYCNTAPPLYINGDATE") == null
							|| assetLineMbo.getDate("PHYCNTAPPLYINGDATE").equals("")) {
						assetMbo.setValue("PHYCNTDATE", assetLineMbo.getDate("PHYCNTAPPLYINGDATE"));
						System.out.println("date is added");
					} else {
						assetMbo.setValue("PHYCNTDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
						System.out.println("date is added");
					}
					assetMbo.setValue("ASSETPHYCNTNUM", assetLineMbo.getString("ASSETPHYCNTNUM"));
					System.out.println("phy num is added ");
					assetMbo.setValue("COMMITTEEID", committeeid);
					System.out.println("commitee is added");
					count++;
					i = 0;
					save();
				}
				if (lines.count() == count) {
					System.out.println("count == " + count);
					super.save();
					return;
				}
			}
			super.save();
		}
		super.save();

	}

}
/*
 * String statment = "siteid = '" + siteid +"' and ASSETPHYCNTNUM ='" +
 * assetphycntnum + "' and isfound =1"; System.out.println("statement : " +
 * statment); SqlFormat sqlFormat = new SqlFormat(this.getUserInfo(), statment);
 * MboSetRemote mboSet = this.getMboSet("$ASSETPHYCNTLINE", "ASSETPHYCNTLINE",
 * sqlFormat.format()); System.out.println("count of asset: " + mboSet.count());
 */