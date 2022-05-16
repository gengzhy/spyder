package xin.cosmos.common;

/**
 * 常量类
 */
public interface Constant {

    /**
     * 文件上传元数据Redis存储统一前缀
     */
    String META_DATA_STORE_PREFIX = "meta_data_store::";
    static String getMetaDataStoreKey(String busiType) {
        return META_DATA_STORE_PREFIX + busiType;
    }

    /**
     * 记录某个业务类型的文件上一次下载成功的数据下标统一前缀
     */
    String DOWNLOAD_OK_lIST_INDEX_PREFIX = "download_ok_list_index::";
    static String getDownloadOklistIndexKey(String busiType) {
        return DOWNLOAD_OK_lIST_INDEX_PREFIX + busiType;
    }
}
