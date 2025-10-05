import React, { memo } from 'react';
import classnames from 'classnames';
import styles from './index.less';

interface IProps {
  className?: string;
}

export default memo<IProps>((props) => {
  const { className } = props;
  return <div className={classnames(styles.operationalDataBar, className)}>
    operationalDataBar
  </div>;
});
