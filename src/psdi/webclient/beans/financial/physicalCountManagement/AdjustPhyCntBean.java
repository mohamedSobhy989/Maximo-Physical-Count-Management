package psdi.webclient.beans.financial.physicalCountManagement;

import java.rmi.RemoteException;

import psdi.app.inventory.InvBalancesSetRemote;
import psdi.mbo.MboRemote;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;

public class AdjustPhyCntBean extends DataBean {

	public AdjustPhyCntBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public synchronized int execute() throws MXException, RemoteException {
		System.out.println("execute fun .. adjust new physicalcount ");
		MboRemote owner = getMbo().getOwner();
		System.out.println("owner " + owner);
		InvBalancesSetRemote invBal = (InvBalancesSetRemote)owner.getMboSet("INVBALANCES");
        System.out.println("count ::: " + invBal.count());
		if (invBal != null) {
			System.out.println("condition");
            invBal.adjustPhysicalCount();
            invBal.totalPhyscntErrors();
            this.clientSession.addMXWarnings(invBal.getWarnings());
            this.clientSession.handleMXWarnings(false);
            System.out.println("save-----------");
            this.save();
             
        }

		
		
		return super.execute();
	}

}
