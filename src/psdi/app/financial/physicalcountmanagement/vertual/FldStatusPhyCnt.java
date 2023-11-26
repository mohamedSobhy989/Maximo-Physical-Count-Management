
package psdi.app.financial.physicalcountmanagement.vertual;

import java.rmi.RemoteException;

import psdi.mbo.*;
import psdi.util.MXException;

public class FldStatusPhyCnt extends SynonymDomain
{

    public FldStatusPhyCnt(MboValue mv)throws RemoteException, MXException
    {
        super(mv);
        flag = 0;
        System.out.println("---- constractor ------");
        setDomainId("ITEMPHYCNTSTATUS");
    }

    public void init()throws MXException, RemoteException
    {
        System.out.println("int dropdown list ");
        super.init();
    }

    public MboSetRemote getList()throws MXException, RemoteException
    {
        System.out.println("here get list = ");
        
        MboRemote statefulOwner = getMboValue().getMbo().getOwner();
         
        System.out.println("the stateful..3 owner "+statefulOwner);
        String status = statefulOwner.getString("status");
        System.out.println((new StringBuilder("the stateful owner  status ")).append(status).toString());
        MboSetRemote statusList = getDomainInfo().getDomainObject(mboValue).getList();
        System.out.println((new StringBuilder("the count ")).append(statusList.count()).toString());
        
        //******************************
        String MXstatus = " ";
        int size = statusList.count();
        for (int i = 0 ; i < size ; i ++ ) {

        	if (statusList.getMbo(i).getString("value").equals(status)) 
        	{
        		MXstatus = statusList.getMbo(i).getString("MAXVALUE");
			}
			
		}

        //******************************
        if(MXstatus.equals("WAPPR"))
            if(flag == 0)
            {
                MboSetRemote statusList2 = isWaitingApprove(statusList, status);
                return statusList2;
            } else
            {
                return statusList;
            }
        if(MXstatus.equals("APPR"))
        {
        	System.out.println("approved");
            if(flag == 0)
            {
            	System.out.println("flag = 0 ");
                MboSetRemote statusList3 = isApproved(statusList, status);
                return statusList3;
            } else
            {
                return statusList;
            }
        }
        if(MXstatus.equals("CAN"))
        {
            if(flag == 0)
            {
                MboSetRemote statusList4 = isCancelled(statusList, status);
                return statusList4;
            } else
            {
                return statusList;
            }
        }
        if(MXstatus.equals("CLOSE"))
        {
            if(flag == 0)
            {
                MboSetRemote statusList4 = isClosed(statusList, status);
                return statusList4;
            } else
            {
                return statusList;
            }
        }
        if(MXstatus.equals("PNDREV"))
        {
            if(flag == 0)
            {
                MboSetRemote statusList4 = isPnRevised(statusList, status);
                return statusList4;
            } else
            {
                return statusList;
            }
        }
        
        else
        {
            return statusList;
        }
    }

    public MboSetRemote isPnRevised(MboSetRemote statusList, String status) throws MXException, RemoteException
    {
        int count = statusList.count();
        for(int i = 0; i < count; i++)
        {
            System.out.println((new StringBuilder("status value from domain is : ")).append(i).append(" ").append(statusList.getMbo(i).getString("value")).toString());
            System.out.println(statusList.getMbo(i).getString("value").equals(status));
            
           
            if(statusList.getMbo(i).getString("value").equals(status))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            }
            else if(statusList.getMbo(i).getString("MAXVALUE").equals("REVISD"))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            } else
            if(statusList.getMbo(i).getString("MAXVALUE").equals("PNDREV"))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            }
            if(statusList.getMbo(i).getString("MAXVALUE").equals("WAPPR")||statusList.getMbo(i).getString("VALUE").equals("WAPPR"))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            }
        }

        flag = 1;
        return statusList;
    }
    
    public MboSetRemote isCancelled(MboSetRemote statusList, String status)throws MXException, RemoteException
    {
        int count = statusList.count();
        for(int i = 0; i < count; i++)
        {
            System.out.println((new StringBuilder(" is approved : loop ")).append(i).toString());
            statusList.remove(i);
            System.out.println((new StringBuilder(" Removed Done : ")).append(i).toString());
            i--;
            count--;
        }

        flag = 0;
        return statusList;
    }

    public MboSetRemote isClosed(MboSetRemote statusList, String status)throws MXException, RemoteException
    {
        int count = statusList.count();
        for(int i = 0; i < count; i++)
        {
            System.out.println((new StringBuilder(" is approved : loop ")).append(i).toString());
            statusList.remove(i);
            System.out.println((new StringBuilder(" Removed Done : ")).append(i).toString());
            i--;
            count--;
        }

        flag = 0;
        return statusList;
    }
   
    public MboSetRemote isApproved(MboSetRemote statusList, String status)  throws MXException, RemoteException
    {
    	System.out.println("is approved");
        int size = statusList.count();
        for(int i = 0; i < size; i++)
        {
            System.out.println((new StringBuilder(" is approved : loop ")).append(i).toString());
            if(!statusList.getMbo(i).getString("MAXVALUE").equals("CAN") )
            {
            	if(!statusList.getMbo(i).getString("MAXVALUE").equals("CLOSE") )
                {
                    System.out.println((new StringBuilder(" condition : ")).append(i).append(!statusList.getMbo(i).getString("value").equals("CAN")).toString());
                    System.out.println((new StringBuilder(" is approved : cancle : ")).append(i).append(" and status is : ").append(statusList.getMbo(i).getString("value")).toString());
                    statusList.remove(i);
                    System.out.println((new StringBuilder(" Removed Done : ")).append(i).toString());
                    i--;
                    size--;
                }
            }
            System.out.println((new StringBuilder("the size in for loop = ")).append(statusList.count()).toString());
        }

        System.out.println((new StringBuilder("the size = ")).append(statusList.count()).toString());
        System.out.println((new StringBuilder("the size = ")).append(statusList.getMbo(0).getString("value")).toString());
        flag = 1;
        return statusList;
    }

    public MboSetRemote isWaitingApprove(MboSetRemote statusList, String status)throws MXException, RemoteException
    {
        int count = statusList.count();
        for(int i = 0; i < count; i++)
        { 
            System.out.println((new StringBuilder("status value from domain is : ")).append(i).append(" ").append(statusList.getMbo(i).getString("value")).toString());
            System.out.println(statusList.getMbo(i).getString("value").equals(status));
            
           
            if(statusList.getMbo(i).getString("value").equals(status))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            }
            else if(statusList.getMbo(i).getString("MAXVALUE").equals("REVISD"))
            { 
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            } else
            if(statusList.getMbo(i).getString("MAXVALUE").equals("CLOSE"))
            {
                System.out.println("if condition: ");
                statusList.remove(i);
                i--;
                count--;
            }
        }

        flag = 1;
        return statusList;
    }
    int flag;
}