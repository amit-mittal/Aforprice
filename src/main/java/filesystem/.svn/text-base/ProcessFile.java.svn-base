package filesystem;

import java.io.File;

public class ProcessFile implements Runnable{
	private String fileName;
	private String processingDir = "E:\\ProcessingDirectory";
	private String completedDir = "E:\\CompletedDirectory";

	private void moveToProcessingDir(){
		String newFileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		String newPath = processingDir + "\\" + newFileName;

		File afile =new File(fileName);

		if(afile.renameTo(new File(newPath))){
			System.out.println("File is moved successful to ProcessingDir!");
		}else{
			System.out.println("File is failed to move to ProcessingDir!");
		}
	}

	private void moveToCompletedDir(){
		String newFileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		String oldPath = processingDir + "\\" + newFileName;
		String newPath = completedDir + "\\" + newFileName;

		File afile =new File(oldPath);

		if(afile.renameTo(new File(newPath))){
			System.out.println("File is moved successful to CompletedDir!");
		}else{
			System.out.println("File is failed to move to CompletedDir!");
		}
	}

	@Override
	public void run() {
		try {
			System.out.println(fileName);
			Thread.sleep(2000L);

			//move to process dir and doWork
			moveToProcessingDir();
			Thread.sleep(2000L);

			//move to completed dir
			moveToCompletedDir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProcessFile(String fileName){
		this.fileName = fileName;
	}
}
