import React, { memo, useMemo } from "react";
import { Dropdown, Space, Tooltip } from "antd";
import Icon, { ReloadOutlined } from "@ant-design/icons";
import classnames from "classnames";
import styles from "./index.less";
import styles2 from "./styles.less";

// ---- store ----
import { useConnectionStore } from "@/pages/main/store/connection";
import { useWorkspaceStore } from "@/pages/workspace/store";
import { setCurrentConnectionDetails } from "@/pages/workspace/store/common";

// ----- components -----
import Iconfont from "@/components/Iconfont";

// ----- constants/typings -----
import { databaseMap } from "@/constants";

import { IConnectionListItem } from "@/typings/connection";
import { ReactComponent as GroupSvg } from "@/svgr/group.svg";
import { ReactComponent as SyncMetadataSvg } from "@/svgr/sync_metadata.svg";

export default memo(() => {
  const { connectionList } = useConnectionStore((state) => {
    return {
      connectionList: state.connectionList,
    };
  });

  const { currentConnectionDetails } = useWorkspaceStore((state) => {
    return {
      currentConnectionDetails: state.currentConnectionDetails,
    };
  });

  const renderConnectionLabel = (item: IConnectionListItem) => {
    return (
      <div className={classnames(styles.menuLabel)}>
        <span
          className={styles.envTag}
          style={{ background: item.environment.color.toLocaleLowerCase() }}
        />
        <div className={styles.menuLabelIconBox}>
          <Iconfont
            className={classnames(styles.menuLabelIcon)}
            code={databaseMap[item.type]?.icon}
          />
        </div>
        <div className={styles.menuLabelTitle}>{item.alias}</div>
      </div>
    );
  };

  const connectionItems = useMemo(() => {
    return (
      connectionList?.map((item) => {
        return {
          key: item.id,
          label: renderConnectionLabel(item),
          onClick: () => {
            setCurrentConnectionDetails(item);
          },
        };
      }) || []
    );
  }, [connectionList, currentConnectionDetails]);

  return (
    <>
      <div className={styles2.resourceTree}>
        <div className={styles2.title}>
          <span className={styles2.titleText}>数据库</span>
          <span className={styles2.titleAction}>
            <Space size={8}>
              <Tooltip placement="bottom" title={"分组"}>
                <Icon
                  component={GroupSvg}
                  style={{ color: "var(--icon-color-focus)", fontSize: 14 }}
                />
              </Tooltip>
              <Tooltip placement="bottom" title={"同步元数据"}>
                <span
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    cursor: "pointer",
                    lineHeight: 1
                  }}
                >
                  <SyncMetadataSvg
                    style={{ fontSize: "13px", cursor: "pointer" }}
                  />
                </span>
              </Tooltip>
              
              <Tooltip placement="bottom" title={"刷新"}>
                <ReloadOutlined
                  style={{
                    display: "flex",
                    fontSize: "13px",
                    cursor: "pointer",
                    color: "var(--icon-color-normal)",
                  }}
                />
              </Tooltip>
            </Space>
          </span>
        </div>
      </div>
    </>
  );

  // return (
  //   <Dropdown menu={{ items: connectionItems }} trigger={['click']} overlayClassName={styles.dropdownOverlay}>
  //     <div className={styles.selectConnection}>
  //       {currentConnectionDetails && renderConnectionLabel(currentConnectionDetails)}
  //       <div className={styles.dropDownArrow}>
  //         <Iconfont code="&#xe641;" />
  //       </div>
  //     </div>
  //   </Dropdown>
  // );
});
