package psdi.app.financial.physicalcountmanagement.vertual;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSet;
import psdi.mbo.MboSetRemote;
import psdi.mbo.MboValue;
import psdi.mbo.MboValueAdapter;
import psdi.mbo.SqlFormat;
import psdi.util.MXException;

public class FldPersonID extends MboValueAdapter{

	public FldPersonID(MboValue mbv) {
		super(mbv);
		System.out.println("FldPersonID");
	}
	
	 @Override
	public void init() throws MXException, RemoteException {
		// TODO Auto-generated method stub
		super.init();
	}
	 
	 @Override
	public void action() throws MXException, RemoteException {

		 String personid = getMboValue().getString();
		 System.out.println("personid = " + personid);
		 Mbo mbo = getMboValue().getMbo();
		 String statement = "personid = '" + personid + "'";
		 
		 SqlFormat sql = new SqlFormat(mbo.getUserInfo(), statement);
		MboSetRemote person = mbo.getMboSet("$PERSON","PERSON", sql.format());
		System.out.println("count = " + person.count()+" person in sql format is  : " + person + " and his department is : " + person.getMbo(0).getString("department"));
			 
		mbo.setValue("name", person.getMbo(0).getString("displayname"));
		mbo.setValue("TITLE", person.getMbo(0).getString("TITLE"));
		mbo.setValue("DEPARTMENT", person.getMbo(0).getString("DEPARTMENT"));
 		
		mbo.setFieldFlag("person", READONLY, true);
		mbo.setFieldFlag("name", READONLY, true);
		mbo.setFieldFlag("title", READONLY, true);
		mbo.setFieldFlag("DEPARTMENT", READONLY, true);
		
		super.action();
	}

}
