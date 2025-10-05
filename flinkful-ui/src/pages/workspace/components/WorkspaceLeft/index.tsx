import React, { memo } from "react";
import { useIntl } from "@umijs/max";

import classnames from "classnames";
import styles from "./index.less";
import TableList from "../TableList";
import WorkspaceLeftHeader from "../WorkspaceLeftHeader";
import Iconfont from "@/components/Iconfont";
import { useConnectionStore } from "@/pages/main/store/connection";
import { setMainPageActiveTab } from "@/pages/main/store/main";

const WorkspaceLeft = memo(() => {
  const intl = useIntl();
  const { connectionList } = useConnectionStore((state) => {
    return {
      connectionList: state.connectionList,
    };
  });

  const jumpPage = () => {
    setMainPageActiveTab("connections");
  };

  return (
    <>
      <div className={classnames(styles.workspaceLeft)}>
        <WorkspaceLeftHeader />
        <TableList />
      </div>
    </>
  );
});

export default WorkspaceLeft;
