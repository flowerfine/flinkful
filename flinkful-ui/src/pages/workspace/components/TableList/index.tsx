import React, { memo, useEffect, useState } from "react";
import styles from "./index.less";
import classnames from "classnames";

import { useWorkspaceStore } from "@/pages/workspace/store";

// ----- components -----
import OperationLine from "../OperationLine";

import Tree from "@/blocks/Tree";
import { treeConfig } from "@/blocks/Tree/treeConfig";
import { ITreeNode } from "@/typings";
import { DatabaseTypeCode, TreeNodeType } from "@/constants";
import { CatalogService } from "@/services/flinkful/catalog.service";

interface IProps {
  className?: string;
}

export default memo<IProps>((props) => {
  const { className } = props;
  const [treeData, setTreeData] = useState<ITreeNode[] | null>(null);

  const [searchValue, setSearchValue] = useState<string>("");

  const currentConnectionDetails = useWorkspaceStore(
    (state) => state.currentConnectionDetails
  );

  const getTreeData = (refresh = false) => {
    if (!currentConnectionDetails?.id) {
      setTreeData([]);
      return;
    }
    // 配置化的树节点类型
    // const treeNodeType = currentConnectionDetails.supportDatabase
    //   ? TreeNodeType.DATA_SOURCE
    //   : TreeNodeType.DATABASE;
    const treeNodeType = TreeNodeType.FLINK_SQL_GATEWAY;
    setTreeData(null);
    
    treeConfig[treeNodeType]
      .getChildren?.({
        refresh: refresh,
        extraParams: {
          databaseType: DatabaseTypeCode.FLINK_SQL_GATEWAY,
        },
      })
      .then((res) => {
        setTreeData(res);
      })
      .catch(() => {
        setTreeData([]);
      });
  };

  useEffect(() => {
    getTreeData();
  }, [currentConnectionDetails]);

  return (
    <div className={classnames(styles.treeContainer, className)}>
      <OperationLine
        getTreeData={getTreeData}
        searchValue={searchValue}
        setSearchValue={setSearchValue}
      />
      <Tree
        className={styles.treeBox}
        searchValue={searchValue}
        treeData={treeData}
      />
    </div>
  );
});
