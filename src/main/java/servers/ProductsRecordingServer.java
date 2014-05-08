//package servers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import work.managers.ProductsRecordWorkManager;
//import work.managers.WorkManager;
//import work.workers.ScheduledWorker;
//import work.workers.specs.ScheduledWorkerSpecs;
//import entities.Retailer;
//
//public class ProductsRecordingServer {
//	public static void main(String[] args){
//		List<String> retailers = new ArrayList<String>();
//		retailers.add(Retailer.ID.WALMART);
//		WorkManager workManager = new ProductsRecordWorkManager(retailers);
//		ScheduledWorker worker = new ScheduledWorker(new ScheduledWorkerSpecs(), workManager);
//		worker.scheduleNow();
//	}
//}
