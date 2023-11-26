package psdi.app.failure;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;
import psdi.app.failure.virtual.ShowAllProblemsSet;
import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValueInfo;
import psdi.mbo.SqlFormat;
import psdi.util.MXApplicationException;
import psdi.util.MXException;
import psdi.util.TenantLevelObj;

public class FailureList extends Mbo implements FailureListRemote {
    public boolean inDuplicate = false;
    boolean addedFailureCode;
    MboSetRemote showallproblemsSet = null;
    private static final int SHOWALLPROBLEMS = 0;
    private static Hashtable ht = null;
    private static TenantLevelObj<Hashtable<String, MboRemote>> newfailurecode = null;

    public FailureList(MboSet ms) throws MXException, RemoteException {
        super(ms);
    }

    public void add() throws MXException, RemoteException {
    	System.out.println("add");
        this.getMboValue("FailureList").generateUniqueID();
        MboRemote owner = this.getOwner();
        MboSetRemote childrenSet;
        if (owner != null) {
            if (!owner.isNull("failurelist")) {
                this.setValue("parent", owner.getLong("failurelist"), 11L);
            }

            childrenSet = this.getThisMboSet();
            if (childrenSet.count() >= 1) {
                this.setValue("type", childrenSet.getMbo(1).getString("type"), 11L);
            }
        } else {
            this.setFieldFlag("type", 7L, true);
            childrenSet = this.getMboSet("FAILURECODE");
            childrenSet.setAutoKeyFlag(false);
            MboRemote failure = childrenSet.add();
            if (failure.isAutoKeyed("FailureCode")) {
                failure.generateAutoKey();
                String failurecode = failure.getString("FailureCode");
                this.setValue("FailureCode", failurecode);
            }
        }

    }

    public void init() throws MXException {
    	System.out.println("init");
        this.setFieldFlag("parent", 7L, true);
        this.setFieldFlag("FailureClass", 7L, true);
        this.setFieldFlag("FLCDescription", 7L, true);
        if (!this.toBeAdded()) {
            try {
                this.setFieldFlag("failurelist", 7L, true);
                this.setFieldFlag("failurecode", 7L, true);
                if (this.getString("parent").equals("")) {
                    this.setFieldFlag("type", 7L, true);
                }
            } catch (RemoteException var2) {
            }
        }

    }

    void setAddedFailureCode(boolean value) {
        this.addedFailureCode = value;
    }

    boolean isFailureCodeAdded() {
        return this.addedFailureCode;
    }

    public void canDelete() throws MXException, RemoteException {
    	System.out.println("candelete");
        MboSetRemote frSet;
        if (this.isNull("PARENT")) {
            frSet = this.getMboSet("$WOLOOKUP", "WORKORDER", "failurecode = :failurecode and orgid=:orgid");
            if (!frSet.isEmpty()) {
                throw new MXApplicationException("failure", "NoDelWO");
            }

            MboSetRemote tkSet = this.getMboSet("$TKLOOKUP", "TICKET", "failurecode = :failurecode and assetorgid=:orgid");
            if (!tkSet.isEmpty()) {
                throw new MXApplicationException("failure", "NoDelTK");
            }

            MboSetRemote assetSet = this.getMboSet("$ASSETLOOKUP", "ASSET", "failurecode = :failurecode and orgid=:orgid");
            if (!assetSet.isEmpty()) {
                throw new MXApplicationException("failure", "NoDelAsset");
            }

            MboSetRemote locSet = this.getMboSet("$LOCLOOKUP", "LOCOPER", "failurecode = :failurecode and orgid=:orgid");
            if (!locSet.isEmpty()) {
                throw new MXApplicationException("failure", "NoDelLoc");
            }
        } else {
            frSet = this.getMboSet("$FRLOOKUP", "FAILUREREPORT", "linenum = :failurelist");
            frSet.setFlag(32L, true);
            if (!frSet.isEmpty()) {
                throw new MXApplicationException("failure", "NoDelFR");
            }

            frSet.close();
        }

    }

    public void undelete() throws MXException, RemoteException {
    	System.out.println("undelete");
        MboRemote parent = this.getOwner();
        if (parent.toBeDeleted()) {
            throw new MXApplicationException("failure", "NoUndelFL");
        } else {
            super.undelete();
            this.getMboSet("FAILURECODE").undeleteAll();
            MboSetRemote children = this.getMboSet("CHILDREN");
            int x = 0;

            for(FailureList child = (FailureList)children.getMbo(x); child != null; child = (FailureList)children.getMbo(x)) {
                child.undelete();
                ++x;
            }

        }
    }

    public void delete(long accessModifier) throws MXException, RemoteException {
    	System.out.println("delete");
        this.deleteFailureListNode(accessModifier);
    }

    protected void deleteFailureListNode(long accessModifier) throws MXException, RemoteException {
    	System.out.println("deleteFailureListNode");
        super.delete(accessModifier);
        MboRemote fc = this.getMboSet("FAILURECODE").getMbo(0);
        MboSetRemote children;
        if (fc != null) {
            children = fc.getMboSet("FAILURELIST");
            children.setFlag(32L, true);
            if (children.getMbo(1) == null) {
                fc.delete(2L);
            }

            children.close();
        }

        children = this.getMboSet("CHILDREN");
        int x = 0;

        for(FailureList child = (FailureList)children.getMbo(x); child != null; child = (FailureList)children.getMbo(x)) {
            child.deleteFailureListNode(2L);
            ++x;
        }

        children.close();
        if (newfailurecode != null) {
            newfailurecode = null;
        }

    }

    public MboRemote duplicate() throws MXException, RemoteException {
        MboRemote newflRemote = null;
        newflRemote = this.copy();
        newflRemote.setValueNull("FailureCode", 2L);
        this.inDuplicate = true;
        ((FailureList)newflRemote).inDuplicate = true;
        this.copyChildren(newflRemote);
        return newflRemote;
    }

    private void copyChildren(MboRemote newflRemote) throws RemoteException, MXException {
        MboSetRemote myChildren = this.getMboSet("CHILDREN");
        MboSetRemote newChildren = newflRemote.getMboSet("CHILDREN");
        int x = 0;
        if (myChildren.isEmpty()) {
            newChildren = null;
        } else {
            MboRemote child = myChildren.getMbo(x);
            ((FailureList)child).inDuplicate = true;

            for(long newparent = newflRemote.getLong("failurelist"); child != null; child = myChildren.getMbo(x)) {
                ((FailureList)child).inDuplicate = true;
                MboRemote newChild = child.copy(newChildren);
                newChild.setValue("parent", newparent, 2L);
                ((FailureList)child).copyChildren(newChild);
                ++x;
            }
        }

        myChildren.close();
        myChildren = null;
    }

    public void creatNewFailureHierarchy(String newFailureCode) throws MXException, RemoteException {
        SqlFormat sqlFC = new SqlFormat(this.getUserInfo(), "failurecode = :1 and parent is null ");
        sqlFC.setObject(1, "FAILURELIST", "FAILURECODE", newFailureCode);
        MboSetRemote failureCodeSet = this.getMboSet("$FCLOOKUP", "FAILURELIST", sqlFC.format());
        if (!failureCodeSet.isEmpty()) {
            throw new MXApplicationException("failure", "hierarchiexists");
        } else {
            this.setValue("failurecode", newFailureCode, 2L);
            this.setValueNull("parent", 2L);
            this.setValueNull("type", 2L);
        }
    }

    public void copyFailureCodes(MboSetRemote failureListSet) throws MXException, RemoteException {
        MboSetRemote flSetRemote = this.getMboSet("CHILDREN");
        long flParent = this.getLong("failurelist");
        String type = null;
        if (!flSetRemote.isEmpty()) {
            type = ((FailureList)flSetRemote.getMbo(0)).getString("type");
        }

        int z = 0;

        for(MboRemote listLine = failureListSet.getMbo(z); listLine != null; listLine = failureListSet.getMbo(z)) {
            if (listLine.isSelected()) {
                for(int mboNum = 0; mboNum < flSetRemote.count(); ++mboNum) {
                    if (flSetRemote.getMbo(mboNum).getString("failurecode").equals(listLine.getString("FailureCode"))) {
                        throw new MXApplicationException("failure", "duplicatechild");
                    }
                }

                this.copyFailureHierachy(listLine, flSetRemote, flParent, type);
            }

            ++z;
        }

    }

    private void copyFailureHierachy(MboRemote listline, MboSetRemote flSetRemote, long flParent, String type) throws MXException, RemoteException {
        MboRemote flRemote = null;
        MboRemote flRemoteChildOld = null;
        MboRemote flRemoteChild = null;
        Vector flChild = new Vector();
        Vector flChildSet = new Vector();
        long flOldTop = listline.getLong("failurelist");
        flRemote = listline.copy(flSetRemote);
        flChild.add(0, flRemote);
        flRemote.setValueNull("FailureCode", 2L);
        flRemote.setValue("FailureCode", listline.getString("failurecode"), 2L);
        flRemote.setValue("parent", flParent, 2L);
        if (type != null) {
            flRemote.setValue("type", type, 2L);
        }

        long flNewTop = flRemote.getLong("failurelist");
        Vector newNode = new Vector();
        Vector oldNode = new Vector();
        int head = 0;
        int tail = 1;
        oldNode.add(head, flOldTop);
        newNode.add(head, flNewTop);

        for(int j = 0; head != tail; ++j) {
            long newParent = (long)((Long)newNode.elementAt(head)).intValue();
            MboSetRemote FailureListNewChildren = ((MboRemote)flChild.elementAt(head)).getMboSet("CHILDREN");
            flChildSet.add(head, FailureListNewChildren);
            SqlFormat sqlFL = new SqlFormat(this.getUserInfo(), " parent =:1 ");
            sqlFL.setLong(1, (long)((Long)oldNode.elementAt(head)).intValue());
            MboSetRemote FailureListChildren = this.getMboSet("$FCDLOOKUP", "FAILURELIST", sqlFL.format());
            int k = FailureListChildren.count();

            for(int i = 0; i < k; ++i) {
                flRemoteChildOld = FailureListChildren.getMbo(i);
                if (flRemoteChildOld == null) {
                    break;
                }

                oldNode.add(tail, flRemoteChildOld.getLong("failurelist"));
                flRemoteChild = flRemoteChildOld.copy((MboSetRemote)flChildSet.elementAt(head));
                flRemoteChild.setValue("parent", newParent, 2L);
                flChild.add(tail, flRemoteChild);
                long newKid = flRemoteChild.getLong("failurelist");
                newNode.add(tail, newKid);
                ++tail;
            }

            ++head;
        }

    }

    public void creatNewNode(String newFailureCode) throws MXException, RemoteException {
        MboSetRemote failureCodeSet = this.getMboSet("FAILURECODE");
        MboSetRemote FailureListSet = this.getThisMboSet();
        long flParent = this.getLong("failurelist");
        MboRemote flRemote = FailureListSet.add();
        flRemote.setValue("failurecode", newFailureCode, 2L);
        flRemote.setValue("parent", flParent, 2L);
        if (!this.checkFailureCode(newFailureCode)) {
            MboRemote failCode = null;
            failCode = failureCodeSet.add();
            failCode.setValue("failurecode", newFailureCode, 11L);
        }

    }

    public boolean checkFailureReport(String failureReportCode) throws MXException, RemoteException {
        SqlFormat sqlFC = new SqlFormat(this.getUserInfo(), "failurecode = :1");
        sqlFC.setObject(1, "FAILUREREPORT", "FAILURECODE", failureReportCode);
        MboSetRemote failureReport = this.getMboSet("$FAILUREREPORTLOOKUP", "FAILUREREPORT", sqlFC.format());
        return failureReport.isEmpty();
    }

    public boolean checkFailureCode(String fCode) throws MXException, RemoteException {
        SqlFormat sqfFCODE = new SqlFormat(this.getUserInfo(), "failurecode = :1");
        sqfFCODE.setObject(1, "FAILURECODE", "FAILURECODE", fCode);
        MboRemote newFC = this.getMboSet("$FCODELOOKUP", "FAILURECODE", sqfFCODE.format()).getMbo(0);
        return newFC == null;
    }

    public boolean isTop() throws MXException, RemoteException {
        return this.isNull("PARENT");
    }

    public boolean hasChildren() throws MXException, RemoteException {
        boolean rtn = false;
        MboSetRemote failureL = this.getMboSet("$FAILURELIST" + this.getString("failurecode"), "FAILURELIST", "parent = :failurelist)");
        rtn = !failureL.isEmpty();
        failureL.close();
        return rtn;
    }

    public boolean hasParents() throws MXException, RemoteException {
        return !this.isTop();
    }

    public MboSetRemote getChildren() throws MXException, RemoteException {
        return this.getMboSet("CHILDREN");
    }

    public MboSetRemote getParents() throws MXException, RemoteException {
        return this.getMboSet("PARENT");
    }

    public MboSetRemote getTop() throws MXException, RemoteException {
        FailureList top = this;

        for(MboSetRemote parentSet = this.getParents(); !parentSet.isEmpty(); parentSet = top.getParents()) {
            top = (FailureList)parentSet.getMbo(0);
        }

        return top.getThisMboSet();
    }

    public String[] getHierarchies() throws MXException, RemoteException {
        return null;
    }

    protected boolean skipCopyField(MboValueInfo mvi) throws RemoteException, MXException {
        return mvi.getName().equalsIgnoreCase("FAILURELIST");
    }

    private void initHash() {
        ht = new Hashtable();
        ht.put("SHOWALLPROBLEMS", 0);
    }

    public MboSetRemote getMboSet(String name) throws MXException, RemoteException {
        if (ht == null) {
            this.initHash();
        }

        name = name.toUpperCase();
        Object showNum = ht.get(name);
        int value = -1;
        if (showNum != null) {
            value = (Integer)showNum;
        }

        switch (value) {
            case 0:
                if (this.showallproblemsSet == null) {
                    this.showallproblemsSet = new ShowAllProblemsSet(this);
                    this.showallproblemsSet.setOwner(this);
                }

                return this.showallproblemsSet;
            default:
                return super.getMboSet(name);
        }
    }

    Hashtable<String, MboRemote> getnewfailurecodeHash() {
        if (newfailurecode == null) {
            newfailurecode = new TenantLevelObj();
        }

        if (newfailurecode.get() == null) {
            newfailurecode.set(new Hashtable());
        }

        return (Hashtable)newfailurecode.get();
    }

    public boolean isInDuplicate() throws MXException, RemoteException {
        return this.inDuplicate;
    }
}
