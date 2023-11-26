package psdi.webclient.beans.financial.physicalCountManagement;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.webclient.system.beans.DataBean;
import java.rmi.RemoteException;

public class itemDiscrepancies extends DataBean {

    @Override
    protected void initialize() throws MXException, RemoteException {
        super.initialize();
    }

    @Override
    public synchronized int execute() throws MXException, RemoteException {
        System.out.println("execute");
        MboRemote owner = getMbo().getOwner();
        System.out.println("owner = " + owner);
        if (owner != null) {
            if (owner.getString("status").equals("APPR")) {

                MboSetRemote mboSet = owner.getMboSet("Discrepancies");

                System.out.println("count : " + mboSet.count());
                MboSetRemote mboSetDesc = owner.getMboSet("itemDiscrepancies");
                System.out.println("count : " + mboSetDesc.count());

                for (int i = 0; i < mboSet.count(); i++) {
                    int oldphycnt = mboSet.getMbo(i).getInt("oldphycnt");
                    int newphycnt = mboSet.getMbo(i).getInt("newphyscnt");
                    System.out.println("oldphycnt : " + oldphycnt + " : and new phycnt = " + newphycnt);
                    MboRemote mbo = mboSetDesc.add();
                    if ((mboSet.getMbo(i).getString("itemnum") == null || mboSet.getMbo(i).getString("itemnum").equals("")) &&
                            (mboSet.getMbo(i).getString("description") == null || mboSet.getMbo(i).getString("description").equals(""))) {
                        throw new MXApplicationException("status", "Item  num and description  is null  ");
                    }
                    if (newphycnt > oldphycnt || newphycnt < oldphycnt) {
                        mbo.setValue("descstatus","NOTMATCH");
                    }
                    if (mboSet.getMbo(i).getString("status") != null&&  mboSet.getMbo(i).getInt("newphyscnt")!=0&&mboSet.getMbo(i).getInt("oldphycnt")!=0){
                        mbo.setValue("descstatus","ITEMMISSED");
                    }
                    if (mboSet.getMbo(i).getString("status") == null &&  mboSet.getMbo(i).getInt("newphyscnt")>0){
                        mbo.setValue("descstatus","NEWITEM");
                    }
                    // descripancies status --> NOTMATCH, ITEMMISSED , NEWITEM

                    mbo.setValue("itemnum", mboSet.getMbo(i).getString("itemnum"));
                    mbo.setValue("description", mboSet.getMbo(i).getString("description"));
                    mbo.setValue("binnum", mboSet.getMbo(i).getString("binnum"));
                    mbo.setValue("newphyscnt", mboSet.getMbo(i).getString("newphyscnt"));
                    mbo.setValue("itemphycntnum", mboSet.getMbo(i).getString("itemphycntnum"));
                    mbo.setValue("ITEMPHYCNTLINEID", mboSet.getMbo(i).getString("ITEMPHYCNTLINEID"));
                    mbo.setValue("committeeid", owner.getString("committeeid"));
                }
            }
        } else
            throw new MXApplicationException("status", "Item Physical Count is not Approved");

        return super.execute();
    }
}
