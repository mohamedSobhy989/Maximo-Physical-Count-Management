package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;
import java.util.Date;

import psdi.app.financial.physicalcountmanagement.itemPhysicalCount;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class ChangeStatusBean extends DataBean {

	public ChangeStatusBean() {
		// TODO Auto-generated constructor stub
	}

	protected void initialize() throws MXException, RemoteException {
		System.out.println("===> change status bean class : initialize <======");

		super.initialize();

	}

	@Override
	public int execute() throws MXException, RemoteException {
		// data from owner to test get true data
		MboRemote owner = getMbo().getOwner();

		String itemnum = owner.getString("ITEMPHYCNTNUM");
		String status = owner.getString("STATUS");
		String siteid = owner.getString("Siteid");
		String committeeid = owner.getString("committeeid");
		System.out.println("itemnum is : " + itemnum + " status is :" + status + " siteid is :  " + siteid
				+ " committeeid = " + committeeid);

		MboSetRemote lines = owner.getMboSet("lines");
		System.out.println("count of lines = " + lines.count());
		// new data of status
		Date statDate = getDate("NEWSTATDATE");
		String memo = getString("MEMO");
		String newStatus = getString("status");

		System.out.println(
				"new status is : " + newStatus + " .. status Date is : " + statDate + " .. and Memo is : " + memo);
		if (statDate.equals("") || statDate == null) {
			statDate = MXServer.getMXServer().getDate();

		}

		System.out.println(statDate);

		if (newStatus.equals("") || newStatus == null) {
			System.out.println("status is null");
		} else {
			if (newStatus.equals("APPR")) {
				if (committeeid.equals("") || committeeid == null)
					throw new MXApplicationException("phycnt", "cannotapprove");
				else {
					if (lines.count() == 0)
						throw new MXApplicationException("phycnt", "cantapprove");
					else
						((itemPhysicalCount) owner).changeStatus(newStatus, statDate);
				}
			} else if (newStatus.equals("CAN") || newStatus.equals("CLOSE")) {
				((itemPhysicalCount) owner).changeItemLoced();
				((itemPhysicalCount) owner).changeStatus(newStatus, statDate);
			}

		}

		return super.execute();

	}

	@Override
	public synchronized void save() throws MXException {
		System.out.println("Save");
		super.save();
	}

}
