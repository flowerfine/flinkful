import React from "react";
import { useWorkspaceStore } from "@/pages/workspace/store";
import { GlobalComponents } from "../config";
import styles from "./index.less";

const GlobalExtendComponents = () => {
  const { currentWorkspaceGlobalExtend } = useWorkspaceStore((state) => {
    return {
      currentWorkspaceGlobalExtend: state.currentWorkspaceGlobalExtend,
    };
  });

  switch (currentWorkspaceGlobalExtend?.code) {
    case GlobalComponents.view_ddl:
      return (
        <div className={styles.viewDDLBox}>
          <div
            className={styles.viewDDLHeader}
          >{`${currentWorkspaceGlobalExtend.uniqueData.tableName}-DDL`}</div>
        </div>
      );
    default:
      return <div className={styles.noInformation}>No information</div>;
  }
};

export default GlobalExtendComponents;
