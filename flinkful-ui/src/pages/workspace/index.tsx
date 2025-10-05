import React, { memo, useCallback, useEffect, useRef } from "react";
import { PageContainer } from "@ant-design/pro-components";
import classnames from "classnames";
import { useWorkspaceStore } from "@/pages/workspace/store";
import { setPanelLeftWidth } from "@/pages/workspace/store/config";
import DraggableContainer from "@/components/DraggableContainer";
import useMonacoTheme from "@/components/MonacoEditor/useMonacoTheme";
import shortcutKeyCreateConsole from "./functions/shortcutKeyCreateConsole";

import styles from "./index.less";
import WorkspaceLeft from "./components/WorkspaceLeft";
import WorkspaceRight from "./components/WorkspaceRight";
import { getConnectionList } from "../main/store/connection";
import getConnectionEnvList from "../main/functions/getConnection";

const workspacePage = memo(() => {
  const draggableRef = useRef<any>();
  const { panelLeft, panelLeftWidth } = useWorkspaceStore((state) => {
    return {
      panelLeft: state.layout.panelLeft,
      panelLeftWidth: state.layout.panelLeftWidth,
    };
  });

  // 编辑器的主题
  useMonacoTheme();

  useEffect(() => {
    getConnectionList();
    getConnectionEnvList();
  }, []);

  // 快捷键
  useEffect(() => {
    shortcutKeyCreateConsole();
  }, []);

  const draggableContainerResize = useCallback((data: number) => {
    setPanelLeftWidth(data);
  }, []);

  return (
    <PageContainer title={false}>
      <div className={styles.workspace}>
        <DraggableContainer
          className={styles.workspaceMain}
          onResize={draggableContainerResize}
        >
          <div
            ref={draggableRef}
            style={{ "--panel-left-width": `${panelLeftWidth}px` } as any}
            className={classnames(
              { [styles.hiddenPanelLeft]: !panelLeft },
              styles.boxLeft
            )}
          >
            <WorkspaceLeft />
          </div>
          <WorkspaceRight />
        </DraggableContainer>
      </div>
    </PageContainer>
  );
});

export default workspacePage;
