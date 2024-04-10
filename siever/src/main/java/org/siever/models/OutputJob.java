package org.siever.models;

import java.util.Objects;

public class OutputJob {

    private String S3FileName;

    public OutputJob() {
    }

    public String getS3FileName() {
        return S3FileName;
    }

    public void setS3FileName(String S3FileName) {
        this.S3FileName = S3FileName;
    }

    @Override
    public String toString() {
        return "OutputJob{S3FileName='" + S3FileName + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutputJob outputJob = (OutputJob) o;
        return Objects.equals(S3FileName, outputJob.S3FileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(S3FileName);
    }
}
