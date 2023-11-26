package psdi.app.financial.physicalcountmanagement;

import java.rmi.RemoteException;
import java.util.Date;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.server.AppService;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class itemPhysicalCount extends Mbo {

	public itemPhysicalCount(MboSet ms) throws RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws MXException {

		setFieldFlag("CREATEBY", READONLY, true);
		setFieldFlag("CREATEDATE", READONLY, true);
		setFieldFlag("ORGID", READONLY, true);
		setFieldFlag("SITEID", READONLY, true);
		setFieldFlag("STATUS", READONLY, true);
		setFieldFlag("STATUSDATE", READONLY, true);
		setFieldFlag("STOREROOM", READONLY, true);

		super.init();

	}

	@Override
	public void add() throws MXException, RemoteException {
		// TODO Auto-generated method stub

		setFieldFlag("CREATEBY", READONLY, false);
		setFieldFlag("CREATEDATE", READONLY, false);
		setFieldFlag("ORGID", READONLY, false);
		setFieldFlag("SITEID", READONLY, false);
		setFieldFlag("STATUS", READONLY, false);
		setFieldFlag("STATUSDATE", READONLY, false);
		setFieldFlag("STOREROOM", READONLY, false);
		setFieldFlag("committeeid", READONLY, false);

		setValue("CREATEBY", getUserInfo().getUserName());
		setValue("CREATEDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
		setValue("ORGID", this.getProfile().getDefaultOrg());
		setValue("SITEID", this.getProfile().getDefaultSite());
		setValue("STATUS", "WAPPR");
		setValue("STATUSDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
		setFieldFlag("CREATEBY", READONLY, true);
		setFieldFlag("CREATEDATE", READONLY, true);
		setFieldFlag("ORGID", READONLY, true);
		setFieldFlag("SITEID", READONLY, true);
		setFieldFlag("STATUS", READONLY, true);
		setFieldFlag("STATUSDATE", READONLY, true);
		setFieldFlag("committeeid", READONLY, true);
		super.add();
	}

	public void addCommittee(String itemphycntid, String person, String name, String title, String depart, String role)
			throws RemoteException, MXException {
		System.out.println("add committee");
		System.out.println("person id = " + person + " and his name is : " + name + " and title is " + title);
		// PERSON
		MboSetRemote persons = this.getMboSet("person");
		System.out.println("count = " + persons.count());
		int flag = 0;
		for (int i = 0; i < persons.count(); i++) {
			String personid = persons.getMbo(i).getString("personid");
			String name2 = persons.getMbo(i).getString("name");
			String title2 = persons.getMbo(i).getString("title");
			String role2 = persons.getMbo(i).getString("role");
			String dept = persons.getMbo(i).getString("department");
			String physicalid = persons.getMbo(i).getString("PHYCNTID");
			if (person.equalsIgnoreCase(personid) && name.equalsIgnoreCase(name2) && title.equalsIgnoreCase(title2)
					&& depart.equalsIgnoreCase(dept) && itemphycntid.equals(physicalid)) {
				throw new MXApplicationException("phycnt", "addcommittee");
			} else
				flag = 1;
		}
		System.out.println("flag = " + flag);

		if (flag == 1) {
			System.out.println("will add");
			MboRemote mbo = persons.add();
			mbo.setValue("PERSONID", person);
			mbo.setValue("NAME", name);
			mbo.setValue("ROLE", role);
			mbo.setValue("DEPARTMENT", depart);
			mbo.setValue("TITLE", title);
			mbo.setValue("PHYCNTID", itemphycntid);
			mbo.setValue("ORGID", getProfile().getDefaultOrg());
			mbo.setValue("SITEID", getProfile().getDefaultSite());
			super.save();
		}
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

	public void applyPhyCnt() throws RemoteException, MXException {
		System.out.println("applyPhyCnt..........");
		MboSetRemote lines = this.getMboSet("PHYCNTAPPLYING");// itemphycntline
		lines.setOrderBy("INVBALANCESID asc");
		System.out.println("count of lines = " + lines.count());
		System.out.println("itemnum of lines = " + lines.getMbo(0).getString("itemnum") + " and physcnt = "
				+ lines.getMbo(0).getString("newphyscnt"));

		MboSetRemote invBalances = this.getMboSet("INVBALANCES");// invbalances
		System.out.println("count of invBalances = " + invBalances.count());
		int count = 0;
		String[] params = { getString("status"), "Update" };
		if (getString("status").equals("APPR")) {

			for (int i = 0; i < invBalances.count(); i++) {
				String LineInvBal = lines.getMbo(count).getString("INVBALANCESID");
				String InvBalanceID = invBalances.getMbo(i).getString("INVBALANCESID");
				System.out.println("line invBal = " + LineInvBal + " and invBal id = " + InvBalanceID);
				if (LineInvBal.equals(InvBalanceID)) {
					MboRemote line = lines.getMbo(count);
					MboRemote invBal = invBalances.getMbo(i);
					// System.out.println("old phycnt = " +
					// invBalances.getMbo(i).getString("PHYSCNT"));
					// System.out.println("new phycnt = " + line.getString("NEWPHYSCNT"));
					// invBal.setFlag(READONLY, false);
					invBal.setFieldFlag("PHYSCNT", READONLY, false);
					invBal.setFieldFlag("PHYSCNTDATE", READONLY, false);
					invBal.setFieldFlag("RECONCILED", READONLY, false);
					invBal.setValue("ADJUSTEDPHYSCNT", line.getString("NEWPHYSCNT"));
					invBal.setValue("ADJUSTEDPHYSCNTDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					invBal.setValue("PHYSCNTDATE", ((AppService) this.getMboServer()).getMXServer().getDate());
					invBal.setValue("PHYSCNT", line.getString("NEWPHYSCNT"));
					invBal.setValue("RECONCILED", 0);
					// invBal.setFieldFlag("PHYSCNT", READONLY, true);
					System.out.println("end of condition ...... added " + count);
					count++;
					save();

				}
				if (count == lines.count())
					return;
			}
			super.save();
		} else
			throw new MXApplicationException("phycnt", "deleteitem", params);

		super.save();
	}

	public void changeItemLoced() throws RemoteException, MXException {
		System.out.println("ChangeLocked Flag");
		MboSetRemote lines = getMboSet("LINES");
		System.out.println("count ---- > " + lines.count());
		for (int i = 0; i < lines.count(); i++) {
			System.out.println(lines.getMbo(i).getString("locked"));
			lines.getMbo(i).setValue("locked", 0);
			System.out.println("item " + lines.getMbo(i).getString("itemnum") + "is unlocked");
		}

	}

}
