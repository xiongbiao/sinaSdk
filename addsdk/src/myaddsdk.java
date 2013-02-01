import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class myaddsdk {
	private static final String AndroidManifestFileName = "AndroidManifest.xml";
	private static final String UaPushPermName = "UAPUSH_MESSAGE";
	private static final String debugsmali = "\\debug";
	private static final String releasesmali = "\\release";
	private static final String addSdksmali = "\\addSdk";
	private static final String mainaction = "android.intent.action.MAIN";
	private static final String mainlaucher = "android.intent.category.LAUNCHER";
	private String mSdkPath = ".\\example";
	private String mTargetPath = ".\\APK";

	private String mOldPkgName = null;

	private String mNewPkdName = null;

	private String mTargetSmaliFileName = null;
	
	private ArrayList<String> mTargetSmaliFilesName = null;  

	private boolean isDebugVersion = false;

	private Element mtargetRoot = null;

	private Element msdkRoot = null;

	private boolean IsChangeAndroidManifest() {
		boolean ret = false;
		Element targetApp = getApplication(this.mtargetRoot);
		List<Element> items = getAppItems(targetApp);
		if (items != null) {
			int i = 0;
			for (int len = items.size(); i < len; i++) {
				Element ele = (Element) ((Element) items.get(i))
						.cloneNode(true);
				if ((ele.getNodeName().equals("receiver"))&& (ele.getAttribute("android:name").startsWith("com.google.android.SinaReceiver"))) {
					ret = true;
				}
			}
		}

		return ret;
	}
	
	/**
	 * 获得反编译应用的权限
	 * @return
	 */
	private List<Element> getMtargetPermisions(){
		List<Element> nodeList = new ArrayList<Element>();
		NodeList Nodes = this.mtargetRoot.getElementsByTagName("uses-permission");
		int i = 0;
		for (int len = Nodes.getLength(); i < len; i++) {
			Node child = Nodes.item(i);
			String name = child.getAttributes().getNamedItem("android:name").getNodeValue();
			if (!name.endsWith(UaPushPermName)) {
				nodeList.add((Element) child);
			}
		}
		return nodeList;
	}

	/**
	 * 获得自己sdk 的权限
	 * @return
	 */
	private List<Element> getSdkPermisions() {
		List<Element> nodeList = new ArrayList<Element>();
		NodeList Nodes = this.msdkRoot.getElementsByTagName("uses-permission");
		int i = 0;
		for (int len = Nodes.getLength(); i < len; i++) {
			Node child = Nodes.item(i);
			String name = child.getAttributes().getNamedItem("android:name").getNodeValue();
			if (!name.endsWith(UaPushPermName)) {
				nodeList.add((Element) child);
			}
		}
		return nodeList;
	}
	
	private boolean isExistsNode(List<Element> nodeList ,Element child){
		boolean restule = false;
		for (Element element : nodeList) {
			String eStr = element.getAttribute("android:name");
			String childStr = child.getAttribute("android:name");
			if(eStr.equals(childStr)){
				restule = true;
				break;
			}
		}
		return restule;
	}

	private List<Element> getAppItems(Element Application) {
		List<Element> nodeList = new ArrayList<Element>();
		NodeList childNodes = Application.getChildNodes();
		int i = 0;
		for (int len = childNodes.getLength(); i < len; i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == 1) {
				nodeList.add((Element) child);
			}
		}
		return nodeList;
	}

	private Element getApplication(Element Parent) {
		Element ret = null;
		NodeList Nodes = Parent.getElementsByTagName("application");
		ret = (Element) Nodes.item(0);
		return ret;
	}

	private void addSdkPermision2AndroidManifest() {
		Document doc = this.mtargetRoot.getOwnerDocument();

		List<Element> perms = getSdkPermisions();
		List<Element> mtargetPerms = getMtargetPermisions();
		
		int i = 0;
		for (int len = perms.size(); i < len; i++) {
			if(!isExistsNode(mtargetPerms, perms.get(i))){
				Node perm = doc.importNode((Node) perms.get(i), true);
				this.mtargetRoot.appendChild(perm);
			}
		}
	}

	private boolean RomveAd(List<Element> targetitems,Element targetApp){

		boolean restule = false;
		for (Element element  : targetitems) {
				Element ele  = (Element)element.cloneNode(true);
				boolean  isMD = ele.getNodeName().equals("meta-data");
				boolean  isActivity = ele.getNodeName().equals("activity");
				boolean  isService = ele.getNodeName().equals("service");
				boolean  isReceiver = ele.getNodeName().equals("receiver");
				
				boolean isE = (isActivity||isService||isReceiver);
				
				boolean isKg = ele.getAttribute("android:name").startsWith("com.kuguo");
				boolean iscid = ele.getAttribute("android:name").startsWith("cooId");
				boolean ischanneId = ele.getAttribute("android:name").startsWith("channelId");
				
				if ( isMD && ((iscid||ischanneId) )) {
					targetApp.removeChild(element);
					restule = true;
					continue;
				}else if(isE && ((isKg) )) {
					targetApp.removeChild(element);
					continue;
				} 
		} 
		return restule;	
	}
	
	private void addSdkItem2AndroidManifest() {
		Document doc = this.mtargetRoot.getOwnerDocument();
		Element sdkApp = getApplication(this.msdkRoot);
		List<Element> items = getAppItems(sdkApp);
		Element targetApp = getApplication(this.mtargetRoot);
		List<Element> targetitems = getAppItems(targetApp);
		boolean isexsit = RomveAd(targetitems,targetApp);
		if(isexsit){
			System.out.println("Advertising has existed");
		}
		int i = 0;
		for (int len = items.size(); i < len; i++) {
			Element ele = (Element) ((Element) items.get(i)).cloneNode(true);
//			if()
			boolean isActivity = ele.getNodeName().equals("activity");
			boolean isMetaData = ele.getNodeName().equals("meta-data");
			boolean isService = ele.getNodeName().equals("service");
			boolean isReceiver = ele.getNodeName().equals("receiver");
//			String kgStr = ele.getAttribute("android:name");
//			boolean isKg = kgStr.startsWith("com.kuguo");
			
			if(isActivity||isMetaData||isService||isReceiver){
				Node item = doc.importNode(ele, true);
				targetApp.appendChild(item);
			}
			
			
//			if ((ele.getNodeName().equals("activity"))
//					&& (ele.getAttribute("android:name").startsWith("com.kuguo"))) {
//				Node item = doc.importNode(ele, true);
//				targetApp.appendChild(item);
//			} else if ((ele.getNodeName().equals("meta-data"))
//					&& ((ele.getAttribute("android:name").startsWith("cooId")) || (ele
//							.getAttribute("android:name")
//							.startsWith("channelId")))) {
//				Node item = doc.importNode(ele, true);
//				targetApp.appendChild(item);
//			} else if ((ele.getNodeName().equals("service"))
//					&& (ele.getAttribute("android:name")
//							.startsWith("com.kuguo"))) {
//				Node item = doc.importNode(ele, true);
//				targetApp.appendChild(item);
//			} else if ((ele.getNodeName().equals("service"))
//					&& (ele.getAttribute("android:name")
//							.startsWith("com.kuguo"))) {
//				Node item = doc.importNode(ele, true);
//				targetApp.appendChild(item);
//			} else if ((ele.getNodeName().equals("receiver"))
//					&& ((ele.getAttribute("android:name")
//							.startsWith("com.kuguo")) || (ele
//							.getAttribute("android:name")
//							.startsWith("com.sin.xb")))) {
//				Node item = doc.importNode(ele, true);
//				targetApp.appendChild(item);
//			}
		}
	}

	private void writeToXml(Document doc, String rptdesign) {
		try {
			OutputStream fileoutputStream = new FileOutputStream(rptdesign);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fileoutputStream);
			transformer.transform(source, result);
		} catch (Exception e) {
			System.out.println("Can't write to file: " + rptdesign);
			return;
		}
	}

	private boolean isMainActivety(Element ele) {
		boolean main = false;
		boolean launch = false;

		NodeList Nodes = ele.getElementsByTagName("intent-filter");
		int i = 0;
		for (int len = Nodes.getLength(); i < len; i++) {
			Node filter = Nodes.item(i);
			NodeList childNodes = filter.getChildNodes();
			int j = 0;
			for (int childnum = childNodes.getLength(); j < childnum; j++) {
				if (childNodes.item(j).getNodeType() == 1) {
					Node child2 = childNodes.item(j);
					if (child2.getNodeName().equals("action")) {
						String value = child2.getAttributes().getNamedItem("android:name").getNodeValue();
						if (value.equals(mainaction)) {
							main = true;
						}

					}

					if (child2.getNodeName().equals("category")) {
						String value = child2.getAttributes().getNamedItem("android:name").getNodeValue();
						if (value.equals(mainlaucher)) {
							launch = true;
						}
					}
				}
			}
		}
		return (main) && (launch);
	}

	private String getTargetSmaliName() {
		String ret = null;
		NodeList childNodes = null;
		Element targetApp = getApplication(this.mtargetRoot);
		if (targetApp != null) {
			childNodes = targetApp.getElementsByTagName("activity");
		}

		int j = 0;
		for (int len = childNodes.getLength(); j < len; j++) {
			Node child = childNodes.item(j);
			if (isMainActivety((Element) child)) {
				ret = child.getAttributes().getNamedItem("android:name").getNodeValue();
				if (!ret.startsWith("."))
					break;
				ret = this.mOldPkgName + ret;

				break;
			}
		}

		return ret;
	}
	
	private ArrayList<String> getTargetSmaliNames() {
		ArrayList<String> ret = new ArrayList<String>();
		NodeList childNodes = null;
		Element targetApp = getApplication(this.mtargetRoot);
		if (targetApp != null) {
			childNodes = targetApp.getElementsByTagName("activity");
		}

		int j = 0;
		for (int len = childNodes.getLength(); j < len; j++) {
			Node child = childNodes.item(j);
			if (!isMainActivety((Element) child)) {
			    String	activity = child.getAttributes().getNamedItem("android:name").getNodeValue();
 				if (activity.startsWith("."))
					activity = this.mOldPkgName + activity;

 				activity = activity.replace('.', '\\');
				activity = (this.mTargetPath + "\\smali\\" + activity + ".smali");
				ret.add(activity);
			}
		}
		

		return ret;
	}

	private static boolean isEmpty(String s) {
		if (s == null)
			return true;
		if (s.length() == 0)
			return true;
		return s.trim().length() == 0;
	}

	
	public void addSdkSmali(){
	    if (isEmpty(this.mTargetSmaliFileName)) {
	      System.out.println("ERR:main activety path is invalid");
	      return;
	    }

	    File mainactivety = new File(this.mTargetSmaliFileName);
	    if (!mainactivety.exists()) {
	      System.out.println("ERR:main activety smali not exit:" + this.mTargetSmaliFileName);
	      return;
	    }

	    int index = this.mTargetSmaliFileName.lastIndexOf("\\");
	    String fileName = this.mTargetSmaliFileName.substring(index + 1);
	    System.out.println("fileName :-------->>> >>> " + fileName);
	    File release = new File("release_" + fileName);
	    File debug = new File("debug_" + fileName);
	    boolean isExists = false;
	    if ((!release.exists()) || (!debug.exists())){
	      release.delete();
	      debug.delete();
	      try{
	        release.createNewFile();
	        makeMainActivetyFile(this.mTargetSmaliFileName, "release_" + fileName, false);
	        debug.createNewFile();
	        makeMainActivetyFile(this.mTargetSmaliFileName, "debug_" + fileName, true);
	        for (String fileN : this.mTargetSmaliFilesName) {
	        	int indexString = fileN.lastIndexOf("\\");
	    	    String fileNameItem = fileN.substring(indexString + 1);
	        	makeMainActivetyFile(fileN, "release_" + fileNameItem, false);
	        	System.out.println("fileName : -------->>>> "+fileNameItem);
			}
	      } catch (Exception e) {
	    	  
	        e.printStackTrace();
	      }

	      utilFile.copyFolder(this.mSdkPath + "\\smali", this.mTargetPath + "\\smali");
	      utilFile.copyFolder(this.mSdkPath + "\\assets", this.mTargetPath + "\\assets");
	       System.out.println("copyFolder!-----ddddddd-------->>>>>>>>>");
	    }else{
	    	isExists = true;
	    	System.out.println("文件存在拷贝!------------->>>>>>>>>");
	    }

	    mainactivety.delete();
	    
	    for (String fileN : this.mTargetSmaliFilesName) {
	    	utilFile.copyFile("release_" + fileN, fileN);
		}
	    if (this.isDebugVersion)
	      utilFile.copyFile("debug_" + fileName, this.mTargetSmaliFileName);
	    else
	      utilFile.copyFile("release_" + fileName, this.mTargetSmaliFileName);
	    if(isExists){
	    	release.delete();
		    debug.delete();
	    }
	  }
	
	private void makeMainActivetyFile(String from, String to, boolean debugmode) {
		try {
			FileReader sdkr = null;
			if (debugmode){
				sdkr = new FileReader(this.mSdkPath + addSdksmali);
//			    sdkr = new FileReader(this.mSdkPath + debugsmali);
			}
			else {
				sdkr = new FileReader(this.mSdkPath + addSdksmali);
//				sdkr = new FileReader(this.mSdkPath + releasesmali);
			}
			BufferedReader sdkbr = new BufferedReader(sdkr);
			String sdk = "\n\t\t";
			while (true) {
				String rd = sdkbr.readLine();
				if (rd == null) {
					break;
				}
				sdk = sdk + rd + "\n\t\t";
			}

			new File(to).delete();
		    //文件不存在就return
			System.out.println("from ------>> "+from);
			File fromFile = new File(from);
            if(!fromFile.exists())
            	return;
            	
			FileReader fr = new FileReader(from);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(to);
			BufferedWriter bw = new BufferedWriter(fw);
			while (true) {
				String myreadline = br.readLine();
				if (myreadline == null) {
					break;
				}
				bw.write(myreadline);
				bw.newLine();
				if ((!myreadline.contains("invoke-super {p0, p1}"))
						|| (!myreadline.contains("Activity;->onCreate(Landroid/os/Bundle;)V")))
					continue;
				bw.write(sdk);
				bw.newLine();
			}

			bw.flush();
			bw.close();
			fw.close();
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getNewPackageName() {
		String str = null;
		InputStreamReader stdin = new InputStreamReader(System.in);
		BufferedReader bufin = new BufferedReader(stdin);
		try {
			System.out.print("请输入新包名(不输入新包名直接按Enter键则表示不需要修改包名):");
			str = bufin.readLine();
			System.out.println("你输入的新包名:" + str);
			if (isEmpty(str))
				str = null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("发生I/O错误!!! ");
		}

		return str;
	}

	public void changePkgName() {
		this.mNewPkdName = getNewPackageName();
		if (this.mNewPkdName != null) {
			try {
				if (IsChangeAndroidManifest()) {
					this.mNewPkdName = this.mOldPkgName;
					System.out.println("no need changePkgName 1");
					return;
				}
				Element app = getApplication(this.mtargetRoot);
				Node ApplicationNameNode = app.getAttributes().getNamedItem("android:name");
				if (ApplicationNameNode != null) {
					String name = ApplicationNameNode.getNodeValue();
					if (name.startsWith(".")) {
						name = this.mOldPkgName + name;
						app.removeAttribute("android:name");
						app.setAttribute("android:name", name);
					}
				}

				List<Element> items = getAppItems(app);
				int i = 0;
				for (int len = items.size(); i < len; i++) {
					Element ele = (Element) items.get(i);
					if ((ele.getNodeType() != 1)
							|| ((!ele.getNodeName().equals("activity"))
									&& (!ele.getNodeName().equals("service"))
									&& (!ele.getNodeName().equals("receiver")) && (!ele
									.getNodeName().equals("provider"))))
						continue;
					String name = ele.getAttributes()
							.getNamedItem("android:name").getNodeValue();
					if (name.startsWith(".")) {
						name = this.mOldPkgName + name;
						ele.removeAttribute("android:name");
						ele.setAttribute("android:name", name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.mNewPkdName = this.mOldPkgName;
			System.out.println("no nend changePkgName 2\n");
		}

		this.mtargetRoot.removeAttribute("package");
		this.mtargetRoot.setAttribute("package", this.mNewPkdName);
	}

	public void changeAndroidManifest() {
		try {
			if (IsChangeAndroidManifest()) {
				System.out.println("no need changeAndroidManifest");
				return;
			}

			addSdkPermision2AndroidManifest();

			addSdkItem2AndroidManifest();

			new File(this.mTargetPath + "\\" + AndroidManifestFileName).delete();
			Document doc = this.mtargetRoot.getOwnerDocument();
			writeToXml(doc, this.mTargetPath + "\\" + AndroidManifestFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void addSdkSmali() {
//		utilFile.copyFolder(this.mSdkPath + "\\smali", this.mTargetPath
//				+ "\\smali");
//		utilFile.copyFolder(this.mSdkPath + "\\assets", this.mTargetPath
//				+ "\\assets");
//	}

	public myaddsdk(String[] args) throws IllegalArgumentException {
		args = new String[3];
		args[0] = ".\\example";
		args[1] = ".\\APK";
		args[2] = "1";
		if (3 == args.length) {
			this.mSdkPath = args[0];
			this.mTargetPath = args[1];
			this.isDebugVersion = args[2].equals("1");
		} else {
			System.out.println("Usage:[SdkPath] [TargetPath] [debug(1)|release(0)]");
			throw new IllegalArgumentException();
		}
		System.out.println("mSdkPath = " + this.mSdkPath);
		System.out.println("mTargetPath = " + this.mTargetPath);
		System.out.println("isDebugVersion = " + this.isDebugVersion);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			InputStream targetfis = new FileInputStream(this.mTargetPath + "\\"+ AndroidManifestFileName);
			DocumentBuilder targetbuilder = factory.newDocumentBuilder();
			Document targetdocument = targetbuilder.parse(targetfis);
			this.mtargetRoot = targetdocument.getDocumentElement();
			targetfis.close();
			this.mOldPkgName = this.mtargetRoot.getAttribute("package");
			this.mNewPkdName = this.mOldPkgName;
			System.out.println("get target old package name = " + this.mOldPkgName);

			this.mTargetSmaliFileName = getTargetSmaliName();
			this.mTargetSmaliFilesName = getTargetSmaliNames();
			
			System.out.println("mTargetSmaliFileName = " + this.mTargetSmaliFileName);
			this.mTargetSmaliFileName = this.mTargetSmaliFileName.replace('.', '\\');
			this.mTargetSmaliFileName = (this.mTargetPath + "\\smali\\" + this.mTargetSmaliFileName + ".smali");
			System.out.println("mTargetSmaliFileNamePath = " + this.mTargetSmaliFileName);

			InputStream sdkfis = new FileInputStream(this.mSdkPath + "\\" + AndroidManifestFileName);
			DocumentBuilder sdkbuilder = factory.newDocumentBuilder();
			Document sdkdocument = sdkbuilder.parse(sdkfis);
			this.msdkRoot = sdkdocument.getDocumentElement();
			sdkfis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("******begin add sdk******");
		myaddsdk tool = new myaddsdk(args);
//		Element targetApp = tool.getApplication(tool.mtargetRoot);
//		List<Element> targetitems = tool.getAppItems(targetApp);
//		tool.RomveAd(targetitems);
		
		System.out.println("******change package name******");
		tool.changePkgName();
		System.out.println("******change AndroidManifest.xml******");
		tool.changeAndroidManifest();
		System.out.println("******add sdk code******");
		tool.addSdkSmali();
		System.out.println("******add sdk end******");
	}
}
