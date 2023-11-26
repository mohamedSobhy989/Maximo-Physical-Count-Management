package psdi.webclient.beans.report;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ibm.tivoli.maximo.oslc.provider.AttachmentStorage;

import psdi.app.doclink.DoclinkServiceRemote;
import psdi.app.report.ReportUtil;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.mbo.SqlFormat;
import psdi.server.MXServer;
import psdi.util.CommonUtil;
import psdi.util.MXException;
import psdi.util.MXSession;
import psdi.util.logging.MXLogger;
import psdi.webclient.system.controller.SessionContext;
import psdi.webclient.system.runtime.WebClientRuntime;
import psdi.webclient.system.session.WebClientSession;

public class PrintAttachedDocs {
	private String appName;
	private SessionContext sessionContext;
	private int totalDocs;
	private int baddocssize;
	private int allprintdocssize;
	private Vector otherDocs = new Vector();
	private HashSet baddocs = new HashSet();
	private Vector pdfdocs = new Vector();
	private Vector allprintabledocs = new Vector();
	private Vector allFileBasedDocs = new Vector();
	private Vector allUrlBasedDocs = new Vector();
	private String URL = "";
	private static final MXLogger myLogger;
	private static String ACTIVEX_ENABLED;
	private static String PRINT_INHERITED_DOCS;

	public PrintAttachedDocs(SessionContext sessionContext, String appName, MboRemote curMbo) {
		System.out.println("constractor");
		this.sessionContext = sessionContext;

		try {
			this.initalize(appName, curMbo);
		} catch (RemoteException var5) {
			var5.printStackTrace();
		} catch (MXException var6) {
			var6.printStackTrace();
		}

	}

	public Vector getPrintableOtherDocs() {
		System.out.println("getPrintableOtherDocs");
		return this.otherDocs;
	}

	public Vector getPrintablePDFDocs() {
		System.out.println("getPrintablePDFDocs");
		return this.pdfdocs;
	}

	public Vector getAllPrintableDocs() {
		System.out.println("getAllPrintableDocs");
		return this.allprintabledocs;
	}

	public HashSet getBadDocs() {
		System.out.println("getBadDocs");
		return this.baddocs;
	}

	public Vector getAllFileBasedDocs() {
		return this.allFileBasedDocs;
	}

	public Vector getAllUrlBasedDocs() {
		return this.allUrlBasedDocs;
	}

	public int getAllPrintableDocsCount() {
		return this.allprintdocssize;
	}

	public int getBadDocsCount() {
		return this.baddocssize;
	}

	public int getTotalDocsCount() {
		return this.totalDocs;
	}

	public void initalize(String appName, MboRemote curMbo) throws RemoteException, MXException {
		System.out.println("initalize");
		this.appName = appName;
		HttpServletRequest request = this.sessionContext.getRequest();
		WebClientRuntime wcr = WebClientRuntime.getWebClientRuntime();
		WebClientSession sc = wcr.getWebClientSession(request);
		this.URL = sc.getMaximoBaseURL();
		boolean isPrintInheritDocsEnabled = MXServer.getMXServer().getProperty(PRINT_INHERITED_DOCS, "0")
				.equalsIgnoreCase("1");
		HashMap ldocs = null;
		if (isPrintInheritDocsEnabled) {
			ldocs = this.getAllLinkedDocs(appName, curMbo);
		} else {
			ldocs = this.getDirectLinkedDocs(appName, curMbo);
		}

		this.otherDocs = (Vector) ldocs.get("otherdocs");
		this.pdfdocs = (Vector) ldocs.get("pdfdocs");
		this.baddocssize = ((HashSet) ldocs.get("baddocs")).size();
		this.allprintdocssize = ((Vector) ldocs.get("alldocs")).size();
		this.totalDocs = this.baddocssize + this.allprintdocssize;
		this.allFileBasedDocs = (Vector) ldocs.get("allfilebaseddocs");
		this.allUrlBasedDocs = (Vector) ldocs.get("allurlbaseddocs");
	}

	private HashMap getAllLinkedDocs(String appName, MboRemote curMbo) throws RemoteException, MXException {
		System.out.println("getAllLinkedDocs");
		MXSession mxs = this.sessionContext.getMXSession();
		boolean isActiveXEnabled = MXServer.getMXServer().getProperty(ACTIVEX_ENABLED, "1").equalsIgnoreCase("1");
		int i = 0;
		MboRemote dlmbo = null;
		Vector otherdocs = new Vector();
		HashSet baddocs = new HashSet();
		Vector allFileBasedDocsLocal = new Vector();
		Vector allUrlBasedDocsLocal = new Vector();
		MboSetRemote apps = mxs.getMboSet("MAXAPPS");
		apps.setQbe("APP", "=" + appName.toUpperCase());
		apps.reset();
		MboRemote appmbo = apps.getMbo(0);
		String tablename = appmbo.getString("MAINTBNAME");
		MboSetRemote msr = null;
		msr = curMbo.getMboSet("DOCLINKS");

		for (ArrayList al = new ArrayList(); (dlmbo = msr.getMbo(i)) != null; ++i) {
			long ownerid = dlmbo.getLong("ownerid");
			String oid = Long.toString(ownerid) + "_" + dlmbo.getString("ownertable") + "_"
					+ dlmbo.getString("document") + "_" + dlmbo.getString("urlname");
			if (!al.contains(oid)) {
				al.add(oid);
				String docpath = dlmbo.getString("weburl");
				String urltype = dlmbo.getString("URLTYPE");
				String doclinkIdStr = null;
				boolean defStore = AttachmentStorage.isDefaultAttachmentStore();
				if (!defStore) {
					String extension = "notfound";
					if (docpath != null) {
						int indexOfExtension = docpath.lastIndexOf(".");
						if (indexOfExtension >= 0) {
							extension = docpath.substring(indexOfExtension);
						}
					}

					doclinkIdStr = "IBMMAXIMODOCLINKID:=" + dlmbo.getUniqueIDValue() + "IBMEXTN=" + extension;
				}

				boolean fileExists = this.validURL(docpath);
				if (!fileExists) {
					String sDocpath = this.URL + docpath;
					fileExists = this.validURL(sDocpath);
				}

				if (fileExists && dlmbo.getBoolean("PRINTTHRULINK")
						&& this.verifyActiveXDocument(docpath, isActiveXEnabled)) {
					if (docpath.indexOf(".pdf") > 1) {
						this.pdfdocs.add(docpath);
						this.allprintabledocs.add(docpath);
						if (urltype.equalsIgnoreCase("URL")) {
							allUrlBasedDocsLocal.add(docpath);
						} else if (doclinkIdStr == null) {
							allFileBasedDocsLocal.add(docpath);
						} else {
							allFileBasedDocsLocal.add(doclinkIdStr);
						}
					} else {
						otherdocs.add(docpath);
						this.allprintabledocs.add(docpath);
						if (urltype.equalsIgnoreCase("URL")) {
							allUrlBasedDocsLocal.add(docpath);
						} else if (doclinkIdStr == null) {
							allFileBasedDocsLocal.add(docpath);
						} else {
							allFileBasedDocsLocal.add(doclinkIdStr);
						}
					}
				} else {
					baddocs.add(docpath);
				}
			}
		}

		HashMap returnDocs = new HashMap();
		returnDocs.put("otherdocs", otherdocs);
		returnDocs.put("baddocs", baddocs);
		returnDocs.put("pdfdocs", this.pdfdocs);
		returnDocs.put("alldocs", this.allprintabledocs);
		returnDocs.put("allfilebaseddocs", allFileBasedDocsLocal);
		returnDocs.put("allurlbaseddocs", allUrlBasedDocsLocal);
		return returnDocs;
	}

	private HashMap getDirectLinkedDocs(String appName, MboRemote curMbo) throws RemoteException, MXException {
		System.out.println("getDirectLinkedDocs");
		MXSession mxs = this.sessionContext.getMXSession();
		DoclinkServiceRemote sr = (DoclinkServiceRemote) mxs.lookup("DOCLINK");
		Properties p = MXServer.getMXServer().getConfig();
		boolean isActiveXEnabled = p.getProperty(ACTIVEX_ENABLED, "1").equalsIgnoreCase("1");
		int i = 0;
		MboRemote dlmbo = null;
		Vector otherdocs = new Vector();
		HashSet baddocs = new HashSet();
		Vector allFileBasedDocsLocal = new Vector();
		Vector allUrlBasedDocsLocal = new Vector();
		MboSetRemote apps = mxs.getMboSet("MAXAPPS");
		apps.setQbe("APP", "=" + appName.toUpperCase());
		apps.reset();
		MboRemote appmbo = apps.getMbo(0);
		String tablename = appmbo.getString("MAINTBNAME");
		MboSetRemote msr = null;
		msr = curMbo.getMboSet("DOCLINKS");

		for (ArrayList al = new ArrayList(); (dlmbo = msr.getMbo(i)) != null; ++i) {
			long ownerid = dlmbo.getLong("ownerid");
			String oid = Long.toString(ownerid) + "_" + dlmbo.getString("ownertable") + "_"
					+ dlmbo.getString("document") + "_" + dlmbo.getString("urlname");
			if (!al.contains(oid)) {
				al.add(oid);
				String docpath = dlmbo.getString("weburl");
				String urltype = dlmbo.getString("URLTYPE");
				String doclinkIdStr = null;
				boolean defStore = AttachmentStorage.isDefaultAttachmentStore();
				if (!defStore) {
					String extension = "notfound";
					if (docpath != null) {
						int indexOfExtension = docpath.lastIndexOf(".");
						if (indexOfExtension >= 0) {
							extension = docpath.substring(indexOfExtension);
						}
					}

					doclinkIdStr = "IBMMAXIMODOCLINKID:=" + Long.toString(dlmbo.getUniqueIDValue()) + "IBMEXTN="
							+ extension;
				}

				boolean fileExists = this.validURL(docpath);
				if (!fileExists) {
					String sDocpath = this.URL + docpath;
					fileExists = this.validURL(sDocpath);
				}

				if (fileExists && dlmbo.getBoolean("PRINTTHRULINK")
						&& this.verifyActiveXDocument(docpath, isActiveXEnabled)) {
					if (docpath.indexOf(".pdf") > 1) {
						this.pdfdocs.add(docpath);
						this.allprintabledocs.add(docpath);
						if (urltype.equalsIgnoreCase("URL")) {
							allUrlBasedDocsLocal.add(docpath);
						} else if (doclinkIdStr == null) {
							allFileBasedDocsLocal.add(docpath);
						} else {
							allFileBasedDocsLocal.add(doclinkIdStr);
						}
					} else {
						otherdocs.add(docpath);
						this.allprintabledocs.add(docpath);
						if (urltype.equalsIgnoreCase("URL")) {
							allUrlBasedDocsLocal.add(docpath);
						} else if (doclinkIdStr == null) {
							allFileBasedDocsLocal.add(docpath);
						} else {
							allFileBasedDocsLocal.add(doclinkIdStr);
						}
					}
				} else {
					baddocs.add(docpath);
				}
			}
		}

		HashMap returnDocs = new HashMap();
		returnDocs.put("otherdocs", otherdocs);
		returnDocs.put("baddocs", baddocs);
		returnDocs.put("pdfdocs", this.pdfdocs);
		returnDocs.put("alldocs", this.allprintabledocs);
		returnDocs.put("allfilebaseddocs", allFileBasedDocsLocal);
		returnDocs.put("allurlbaseddocs", allUrlBasedDocsLocal);
		return returnDocs;
	}

	private boolean validURL(String docpath) throws RemoteException {
		System.out.println("validURL");
		Properties p = MXServer.getMXServer().getConfig();
		String validateURL = p.getProperty("mxe.report.AttachDoc.validateURL", "1").trim();
		if (!validateURL.equals("0") && !validateURL.equalsIgnoreCase("false")) {
			boolean fileExists = false;

			try {
				docpath = this.encodeDocLink(docpath);
				URL u1 = new URL(docpath);
				InputStream is = u1.openStream();
				is.close();
				fileExists = true;
			} catch (IOException var7) {
				myLogger.debug("doclink url io exeption: " + docpath);
			}

			return fileExists;
		} else {
			return true;
		}
	}

	private MboSetRemote getDocLinksSet(String tablename) throws RemoteException, MXException {
		System.out.println("getDocLinksSet");
		MboSetRemote mboset = this.sessionContext.getMXSession().getMboSet("DOCLINKS");
		SqlFormat sqf = new SqlFormat("ownertable = :1");
		sqf.setObject(1, "DOCLINKS", "OWNERTABLE", tablename);
		mboset.setWhere(sqf.format());
		mboset.reset();
		return mboset;
	}

	private boolean verifyActiveXDocument(String docPath, boolean isActiveX) {
		System.out.println("verifyActiveXDocument");
		if (isActiveX || docPath.indexOf(".doc") <= 1 && docPath.indexOf(".xls") <= 1 && docPath.indexOf(".ppt") <= 1
				&& docPath.indexOf(".docx") <= 1 && docPath.indexOf(".xlsx") <= 1 && docPath.indexOf(".pptx") <= 1) {
			return true;
		} else {
			myLogger.debug("****PRINT**** ActiveX documents excluded");
			return false;
		}
	}

	private String encodeDocLink(String docpath) {
		System.out.println("encodeDocLink");
		String prefix;
		String temp;
		int slashIndex;
		if (docpath.startsWith("http://")) {
			prefix = "http://";
			temp = WebClientRuntime.replaceString(docpath, "\\", "/");
			temp = temp.substring(7);
			slashIndex = temp.indexOf("/");
			if (slashIndex != -1) {
				prefix = prefix + temp.substring(0, slashIndex + 1);
				temp = temp.substring(slashIndex + 1);
				temp = CommonUtil.EncodeFilePath(temp);
				docpath = prefix + temp;
			}
		} else if (docpath.startsWith("https://")) {
			prefix = "https://";
			temp = WebClientRuntime.replaceString(docpath, "\\", "/");
			temp = temp.substring(8);
			slashIndex = temp.indexOf("/");
			if (slashIndex != -1) {
				prefix = prefix + temp.substring(0, slashIndex + 1);
				temp = temp.substring(slashIndex + 1);
				temp = CommonUtil.EncodeFilePath(temp);
				docpath = prefix + temp;
			}
		}

		return docpath;
	}

	static {
		myLogger = ReportUtil.REPORT_LOGGER;
		ACTIVEX_ENABLED = "mxe.activex";
		PRINT_INHERITED_DOCS = "mxe.directprint.inherited.attachments";
	}
}
