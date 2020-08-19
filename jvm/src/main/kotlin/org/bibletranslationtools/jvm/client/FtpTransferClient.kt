package org.bibletranslationtools.jvm.client

import io.reactivex.Completable
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.bibletranslationtools.common.client.IFileTransferClient
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException

class FtpTransferClient(
    private val source: File,
    private val targetPath: String
) : IFileTransferClient {

    companion object {
        private const val FTP_SERVER = "localhost"
        private const val FTP_USER = ""
        private const val FTP_PASSWORD = ""
    }

    override fun transfer(): Completable {
        return Completable.fromCallable {
            if (source.isDirectory) throw IllegalArgumentException("Source should not be a directory")

            val ftpClient = FTPClient()
            ftpClient.connect(FTP_SERVER)
            ftpClient.login(FTP_USER, FTP_PASSWORD)
            ftpClient.enterLocalPassiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            val dirsCreated = createFtpDirectories(ftpClient)
            if (dirsCreated) {
                source.inputStream().use {
                    if (!ftpClient.storeFile(source.name, it)) {
                        throw IOException("Transfer of ${source.name} failed!")
                    }
                }
            } else {
                throw IOException("Could not create $targetPath on the server!")
            }

            if (ftpClient.isConnected) {
                ftpClient.logout()
                ftpClient.disconnect()
            }
        }
    }

    private fun createFtpDirectories(ftpClient: FTPClient): Boolean {
        val pathElements = targetPath.split("/")
        for (dir in pathElements) {
            val existed = ftpClient.changeWorkingDirectory(dir)
            if (!existed) {
                val created = ftpClient.makeDirectory(dir)
                if (created) {
                    ftpClient.changeWorkingDirectory(dir)
                } else {
                    return false
                }
            }
        }
        return true
    }
}
