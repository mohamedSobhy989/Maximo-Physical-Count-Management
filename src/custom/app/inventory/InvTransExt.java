package custom.app.inventory;

import java.rmi.RemoteException;
import java.util.Date;

import psdi.app.financial.FinancialServiceRemote;
import psdi.app.inventory.InvTrans;
import psdi.app.inventory.Inventory;
import psdi.app.inventory.InventoryRemote;
import psdi.app.inventory.MatRecTrans;
import psdi.app.item.ItemRemote;
import psdi.app.location.LocationRemote;
import psdi.app.location.LocationSetRemote;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.mbo.SqlFormat;
import psdi.server.AppService;
import psdi.server.MXServer;
import psdi.util.MXApplicationException;
import psdi.util.MXException;

public class InvTransExt extends InvTrans implements InvTransExtRemote {
    double dOldCurBal = 0.0;
    double dNewCurBal = 0.0;
    double dOldTotalCurBal = 0.0;
    double dNewTotalCurBal = 0.0;
    double dOldAvgCost = 0.0;
    double dNewAvgCost = 0.0;
    String sSite = null;
    String sStoreroom = null;
    String sTransType = null;
    int iVoucherNumber = -9999;

    public InvTransExt(MboSet var1) throws MXException, RemoteException {
        super(var1);
    }

    public void init() throws MXException {
        super.init();
    }

    String mboName = "ITEMPHYCNT";
    public void setMboName(String name) {
    	this.mboName = name;
    }
    
    public void add() throws MXException, RemoteException {
       
    	if(mboName.equalsIgnoreCase("ITEMPHYCNT")) {
    		System.out.println("condition is done");
            this.setValue("TRANSDATE", ((AppService) this.getMboServer()).getMXServer().getDate(), 2L);
            this.setValue("ENTERBY", this.getUserName(), 2L);

    	}else{
    		MboRemote owner = this.getOwner();
            if (owner != null && (owner.isBasedOn("INVBALANCES") || owner.getName().equalsIgnoreCase("INVENTORY") || owner.getName().equalsIgnoreCase("MATRECTRANS") || owner.getName().equalsIgnoreCase("INVCOST") || owner.getName().equalsIgnoreCase("ITEM"))) {
                this.setValue("sourcembo", "INVTRANS", 11L);
                Date tDate = ((AppService)this.getMboServer()).getMXServer().getDate();
                this.setValue("TRANSDATE", ((AppService) this.getMboServer()).getMXServer().getDate(), 2L);
                this.setValue("ENTERBY", this.getUserName(), 2L);
                this.setValue("itemnum", owner.getString("itemnum"), 2L);
                this.setValue("itemsetid", owner.getString("itemsetid"), 2L);
                this.setFieldFlag("itemnum", 128L, true);
                this.setFieldFlag("storeloc", 128L, true);
                this.setFieldFlag("transdate", 128L, true);
                this.setFieldFlag("transtype", 128L, true);
                this.setFieldFlag("quantity", 128L, true);
                FinancialServiceRemote fsr = (FinancialServiceRemote)((AppService)this.getMboServer()).getMXServer().lookup("FINANCIAL");
                if (fsr != null) {
                    this.setValue("FINANCIALPERIOD", fsr.getSpecificFinancialPeriod(this.getUserInfo(), tDate, this.getString("orgid")), 2L);
                }

                if (owner.isBasedOn("INVBALANCES")) {
                    this.setValue("storeloc", owner.getString("location"), 2L);
                    this.setValue("binnum", owner.getString("binnum"), 2L);
                    this.setValue("lotnum", owner.getString("lotnum"), 2L);
                } else if (owner.getName().equalsIgnoreCase("INVENTORY")) {
                    this.setValue("storeloc", owner.getString("location"), 2L);
                } else if (owner.getName().equalsIgnoreCase("INVCOST")) {
                    this.setValue("storeloc", owner.getString("location"), 2L);
                } else if (owner.getName().equalsIgnoreCase("MATRECTRANS")) {
                    this.stdRecAdjustment();
                }

            } else {
                throw new MXApplicationException("inventory", "invTransNoAdd");
            }
    	}
    	
    	
    }
    private void stdRecAdjustment() throws MXException, RemoteException {
        MatRecTrans owner = (MatRecTrans)this.getOwner();
        if (owner.getName().equalsIgnoreCase("MATRECTRANS")) {
            this.setValue("itemnum", owner.getString("itemnum"), 2L);
            this.setValue("itemsetid", owner.getString("itemsetid"), 2L);
            this.setValue("storeloc", owner.getString("tostoreloc"), 2L);
            this.setValue("binnum", owner.getString("tobin"), 2L);
            this.setValue("lotnum", owner.getString("tolot"), 2L);
            this.setValue("transdate", owner.getDate("transdate"), 2L);
            this.setValue("physcnt", 0.0, 11L);
            this.setValue("enterby", owner.getString("enterby"), 2L);
            this.setValue("financialperiod", owner.getString("financialperiod"), 2L);
            InventoryRemote invMbo = (InventoryRemote)owner.getSharedInventory(owner.getString("tostoreloc"), owner.getString("siteid"));
            LocationSetRemote locSet = (LocationSetRemote)owner.getMboSet("LOCATIONS");
            LocationRemote locMbo = (LocationRemote)locSet.getMbo(0);
            String glDebitAcct = "";
            if (invMbo != null) {
                glDebitAcct = invMbo.getString("controlacc");
            } else if (locMbo != null) {
                glDebitAcct = locMbo.getString("controlacc");
            }

            String transType;
            if (locMbo != null) {
                transType = locMbo.getString("receiptvaracc");
                this.setValue("glcreditacct", transType, 2L);
            }

            transType = this.getTranslator().toExternalDefaultValue("ITTYPE", "STDRECADJ", this);
            this.setValue("gldebitacct", glDebitAcct, 2L);
            this.setValue("transtype", transType);
        }
    }
    public void appValidate() throws MXException, RemoteException {
        super.appValidate();
        this.sSite = this.getString("siteid");
        this.sStoreroom = this.getString("storeloc");
        if(mboName.equalsIgnoreCase("ITEMPHYCNT")) {
        	this.sTransType = "PCOUNTADJ";
        }else
        	this.sTransType = this.getTranslator().toInternalString("ITTYPE", this.getString("transtype"));
        if (this.sTransType.equalsIgnoreCase("KITCOSTVAR") && !this.isNull("storeloc")) {
            this.iVoucherNumber = this.getVoucher(this.sSite, this.sStoreroom, "KITBREAK", false);
            if (this.iVoucherNumber != -9999) {
                this.setValue("voucherid", this.iVoucherNumber, 11L);
            }
        }

        String var1 = this.getTranslator().toInternalString("ITTYPE", this.getString("transtype"));
        boolean var2 = var1 != null && var1.equalsIgnoreCase("RECBALADJ");
        boolean var3 = var1 != null && var1.equalsIgnoreCase("INSERTITEM");
        if (var2) {
            int var4 = this.getInt("voucherid");
            int var5 = var4 + 1;
            this.updateVoucherSeed(var5, "RECBALADJ");
        } else if (var3 && this.getOwner().getName().equalsIgnoreCase("INVENTORY")) {
            this.setValue("VOUCHERID", this.getVoucherSeed("INSERTITEM"), 2L);
        }

    }

    public void save() throws MXException, RemoteException {
        super.save();
        this.sTransType = this.getTranslator().toInternalString("ITTYPE", this.getString("transtype"));
        if (this.sTransType.equalsIgnoreCase("KITCOSTVAR") && !this.isNull("storeloc")) {
            this.dOldCurBal = this.getDouble("curbal");
            this.dNewCurBal = this.dOldCurBal;
            this.dOldTotalCurBal = 0.0;
            this.dNewTotalCurBal = this.dOldTotalCurBal;
            this.dOldAvgCost = this.getDouble("oldcost");
            this.dNewAvgCost = this.getDouble("newcost");
            this.setValueNull("oldcurbal");
            this.setValueNull("newcurbal");
            this.setValueNull("oldtotalcurbal");
            this.setValueNull("newtotalcurbal");
            this.setValueNull("oldavgcost");
            this.setValueNull("newavgcost");
            this.setValueNull("oldtotalcost");
            this.setValueNull("newtotalcost");
            this.updateVoucher(this.sSite, this.sStoreroom, "KITBREAK", false);
        }

        ItemRemote var1 = (ItemRemote)this.getMboSet("ITEM").getMbo(0);
        MboRemote var2 = this.getThisMboSet().getMbo(0);
        String var3 = var2.getString("transtype");
        if (this.getOwner().getName().equalsIgnoreCase("INVENTORY") && var3.equalsIgnoreCase("INSERTITEM")) {
            String var4 = ((Inventory)this.getOwner()).getCostType();
            this.setValue("OLDCURBAL", 0.0, 2L);
            this.setValue("NEWCURBAL", var2.getDouble("CURBAL"), 2L);
            this.setValue("OLDTOTALCURBAL", 0.0, 2L);
            this.setValue("NEWTOTALCURBAL", var2.getDouble("CURBAL"), 2L);
            if (var1.getString("itemtype").equalsIgnoreCase("ITEM") || var1.getString("itemtype").equalsIgnoreCase("TOOL")) {
                if (!var1.isCapitalized()) {
                    this.setValue("OLDTOTALCOST", 0.0, 2L);
                    this.setValue("NEWTOTALCOST", var2.getDouble("CURBAL") * var2.getDouble("NEWCOST"), 2L);
                    if (var4.equalsIgnoreCase("AVERAGE")) {
                        this.setValue("OLDAVGCOST", 0.0, 2L);
                        this.setValue("NEWAVGCOST", var2.getDouble("NEWCOST"), 2L);
                    } else if (var4.equalsIgnoreCase("LIFO") || var4.equalsIgnoreCase("FIFO") || var4.equalsIgnoreCase("ASSET")) {
                        this.setValue("OLDAVGCOST", "", 2L);
                        this.setValue("NEWAVGCOST", "", 2L);
                    }
                } else if (!var4.equalsIgnoreCase("LIFO") && !var4.equalsIgnoreCase("FIFO") && !var4.equalsIgnoreCase("ASSET")) {
                    this.setValue("OLDAVGCOST", 0.0, 2L);
                    this.setValue("NEWAVGCOST", 0.0, 2L);
                    this.setValue("OLDTOTALCOST", 0.0, 2L);
                    this.setValue("NEWTOTALCOST", 0.0, 2L);
                } else {
                    this.setValue("OLDAVGCOST", "", 2L);
                    this.setValue("NEWAVGCOST", "", 2L);
                    this.setValue("OLDTOTALCOST", 0.0, 2L);
                    this.setValue("NEWTOTALCOST", 0.0, 2L);
                }
            }
        }

        if (var3.equalsIgnoreCase("INSERTITEM")) {
            int var5 = this.getInt("voucherid") + 1;
            this.updateVoucherSeed(var5, "INSERTITEM");
        }

    }

    public MboSetRemote getInvBalanceSet(String var1, String var2, String var3, String var4, String var5, String var6) throws MXException, RemoteException {
        StringBuffer var7 = new StringBuffer();
        SqlFormat var8;
        if (var2 != null && !var2.equals("")) {
            var8 = new SqlFormat(this, "itemnum = :1 and location = :2 and siteid = :3 ");
            var8.setObject(1, "INVBALANCES", "itemnum", var3);
            var8.setObject(2, "INVBALANCES", "location", var2);
            var8.setObject(3, "INVBALANCES", "siteid", var1);
            var7.append(var8.format());
            var8 = null;
        } else {
            var8 = new SqlFormat(this, "itemnum = :itemnunm and location = :location and siteid = :siteid ");
            var7.append(var8.format());
            var8 = null;
        }

        if (var4 != null) {
            if (var4.equals("")) {
                var7.append(" and binnum is null");
            } else {
                var8 = new SqlFormat(this, " and binnum = :1");
                var8.setObject(1, "INVBALANCES", "BINNUM", var4);
                var7.append(var8.format());
                var8 = null;
            }
        }

        if (var5 != null) {
            if (var5.equals("")) {
                var7.append(" and lotnum is null");
            } else {
                var8 = new SqlFormat(this, " and lotnum = :1");
                var8.setObject(1, "INVBALANCES", "LOTNUM", var5);
                var7.append(var8.format());
                var8 = null;
            }
        }

        if (var6 != null) {
            if (var6.equals("")) {
                var7.append(" and conditioncode is null ");
            } else {
                var8 = new SqlFormat(this, " and conditioncode = :1");
                var8.setObject(1, "INVBALANCES", "conditioncode", var6);
                var7.append(var8.format());
                var8 = null;
            }
        }

        MboSetRemote var9 = this.getMboSet("$InvBalance", "INVBALANCES", var7.toString());
        return var9;
    }

    public double getCurrentBalance(String var1, String var2, String var3, String var4, String var5, String var6) throws MXException, RemoteException {
        MboSetRemote var7 = this.getInvBalanceSet(var1, var2, var3, var4, var5, var6);
        return var7 != null && !var7.isEmpty() ? var7.sum("curbal") : 0.0;
    }

    public int getVoucher(String var1, String var2, String var3, boolean var4) throws MXException, RemoteException {
        MboSetRemote var5 = MXServer.getMXServer().getMboSet("VOUCHER", this.getUserInfo());
        var5.setWhere("siteid = '" + var1 + "' and storeloc = '" + var2 + "' and vouchertype = '" + var3 + "'");
        var5.reset();
        MboRemote var6 = var5.getMbo(0);
        if (var6 != null) {
            if (var4) {
                var6.setValue("unusedcount", var6.getInt("unusedcount") + 1, 11L);
                var5.save(2L);
            }

            return var6.getInt("seed");
        } else {
            return -9999;
        }
    }

    public void updateVoucher(String var1, String var2, String var3, boolean var4) throws MXException, RemoteException {
        MboSetRemote var5 = MXServer.getMXServer().getMboSet("VOUCHER", this.getUserInfo());
        var5.setWhere("siteid = '" + var1 + "' and storeloc = '" + var2 + "' and vouchertype = '" + var3 + "'");
        var5.reset();
        MboRemote var6 = var5.getMbo(0);
        if (var6 != null) {
            if (var4) {
                int var7 = var6.getInt("unusedcount");
                if (var7 <= 0) {
                    return;
                }

                var6.setValue("seed", var6.getInt("seed") + 1, 11L);
                var6.setValue("unusedcount", 0, 11L);
            } else {
                var6.setValue("seed", var6.getInt("seed") + 1, 11L);
            }
        }

        var5.save(2L);
    }

    public void clearUnusedVoucher(String var1, String var2, String var3) throws MXException, RemoteException {
        MboSetRemote var4 = MXServer.getMXServer().getMboSet("VOUCHER", this.getUserInfo());
        var4.setWhere("siteid = '" + var1 + "' and storeloc = '" + var2 + "' and vouchertype = '" + var3 + "'");
        var4.reset();
        MboRemote var5 = var4.getMbo(0);
        if (var5 != null) {
            var5.setValueNull("unusedcount");
        }

        var4.save(2L);
    }

    public double getTotalCurBal(MboRemote var1) throws MXException, RemoteException {
        int var2 = 0;
        double var3 = 0.0;
        SqlFormat var5 = new SqlFormat(this, "itemnum = :1 and location = :2 and itemsetid = :3 and siteid = :4 and orgid= :5");
        var5.setObject(1, "INVBALANCES", "itemnum", var1.getString("itemnum"));
        var5.setObject(2, "INVBALANCES", "location", var1.getString("storeloc"));
        var5.setObject(3, "INVBALANCES", "itemsetid", var1.getString("itemsetid"));
        var5.setObject(4, "INVBALANCES", "siteid", var1.getString("siteid"));
        var5.setObject(5, "INVBALANCES", "orgid", var1.getString("orgid"));
        MboSetRemote var6 = ((MboSet)this.getThisMboSet()).getSharedMboSet("INVBALANCES", var5.format());

        for(MboRemote var7 = null; (var7 = var6.getMbo(var2)) != null; ++var2) {
            var3 += var7.getDouble("curbal");
        }

        return var3;
    }

    public int getVoucherSeed(String var1) throws MXException, RemoteException {
        int var2 = 0;

        try {
            MboSetRemote var3 = MXServer.getMXServer().getMboSet("VOUCHER", this.getUserInfo());
            var3.setWhere("vouchertype='" + var1 + "' and storeloc='" + this.getString("storeloc") + "' and siteid='" + this.getString("siteid") + "' and orgid='" + this.getString("orgid") + "'");
            var3.reset();
            if (!var3.isEmpty()) {
                MboRemote var4 = var3.getMbo(0);
                var2 = var4.getInt("seed");
            }
        } catch (Exception var5) {
            System.out.println("Error in GetVoucherSeed DB Connection  " + var5.getMessage());
        }

        return var2;
    }

    public void updateVoucherSeed(int var1, String var2) throws MXException, RemoteException {
        try {
            MboSetRemote var3 = MXServer.getMXServer().getMboSet("VOUCHER", this.getUserInfo());
            var3.setWhere("siteid = '" + this.getString("siteid") + "' and storeloc = '" + this.getString("storeloc") + "' and vouchertype = '" + var2 + "' and orgid='" + this.getString("orgid") + "'");
            var3.reset();
            if (!var3.isEmpty()) {
                MboRemote var4 = var3.getMbo(0);
                var4.setValue("seed", var1, 11L);
                var3.save(2L);
            }
        } catch (Exception var5) {
            System.out.println("Error in the updateVoucherSeed DB Connection  " + var5.getMessage());
        }

    }
}
