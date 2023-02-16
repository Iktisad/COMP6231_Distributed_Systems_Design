/**
 * 
 */
/**
 * @author iktis
 *
 */
module DMTBS_COMP6231 {
	requires java.rmi;
	requires org.json;
	exports server to java.rmi;
	exports server.atwater to java.rmi;
	exports server.outremont to java.rmi;
	exports server.verdun to java.rmi;
}