package psdi.app.financial.physicalcountmanagement.vertual;

import java.rmi.RemoteException;
import java.util.Date;

import psdi.app.common.virtual.ChangeStatus;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.mbo.StatefulMbo;
import psdi.server.MXServer;
import psdi.util.MXException;

public class PhyCntChgStatus extends ChangeStatus {


	public PhyCntChgStatus(MboSet ms) throws MXException, RemoteException {
		super(ms);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void init() throws MXException {
		System.out.println("change status init func.....");
		 super.init(); 
	}


	@Override
	public void add() throws MXException, RemoteException {
		System.out.println("add in change status ");
        Date currentDT = MXServer.getMXServer().getDate();
        System.out.println("current date " + currentDT);
        setFieldFlag("NEWSTATUSDATE", READONLY, false);
        setValue("NEWSTATUSDATE", currentDT );
        System.out.println("setting date");

        
		super.add();
	}
	/*

	@Override
	public void add() throws MXException, RemoteException {
		// TODO Auto-generated method stub
        Date currentDT = MXServer.getMXServer().getDate();
        setFieldFlag("NEWSTATUSDATE", READONLY, false);
        setValue("NEWSTATUSDATE", currentDT );
        StatefulMbo statusOwner = (StatefulMbo)getOwner();
        
        
        if (statusOwner.getTargetStatusOption() != null) 
        {
        	setValue("genstatusaction", false, 11L);
	        String defaultstatus = getTranslator().toExternalDefaultValue("ITEMPHYCNTSTATUS", statusOwner.getTargetStatusOption(), statusOwner);
	        String setdefaultstatus = null;
	        MboSetRemote statuslist = statusOwner.getValidStatusList();
            if(!statuslist.isEmpty())
            {
                setdefaultstatus = statuslist.getMbo(0).getString("value");
                int index = 0;
                MboRemote BusStatusList = statuslist.getMbo(index);
                do
                {
                    if(BusStatusList == null)
                        break;
                    if(BusStatusList.getString("value").equalsIgnoreCase(defaultstatus))
                    {
                        setdefaultstatus = defaultstatus;
                        break;
                    }
                    index++;
                    BusStatusList = statuslist.getMbo(index);
                } while(true);
                setValue("status", setdefaultstatus, 11L);
            }
            if(statusOwner.getOnListTab() && isNull("status"))
            {
                statuslist = statusOwner.getStatusList();
                if(!statuslist.isEmpty())
                {
                    setdefaultstatus = statuslist.getMbo(0).getString("value");
                    int index = 0;
                    MboRemote BusStatusList = statuslist.getMbo(index);
                    do
                    {
                        if(BusStatusList == null)
                            break;
                        if(BusStatusList.getString("value").equalsIgnoreCase(defaultstatus))
                        {
                            setdefaultstatus = defaultstatus;
                            break;
                        }
                        index++;
                        BusStatusList = statuslist.getMbo(index);
                    } while(true);
                    setValue("status", setdefaultstatus, 11L);
                }
            }
			
		}
        
		super.add();
	}
	*/
}
