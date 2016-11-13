package io.linuxserver.davos.web;

public class Transfer {

    private String fileName;
    private long fileSize;
    private boolean directory;
    private Progress progress;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public static class Progress {

        private double percentageComplete;
        private double transferSpeed;

        public double getPercentageComplete() {
            return percentageComplete;
        }

        public void setPercentageComplete(double percentageComplete) {
            this.percentageComplete = percentageComplete;
        }

        public double getTransferSpeed() {
            return transferSpeed;
        }

        public void setTransferSpeed(double transferSpeed) {
            this.transferSpeed = transferSpeed;
        }
    }
}
