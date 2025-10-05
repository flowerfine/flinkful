import React, { memo } from 'react';
import classnames from 'classnames';

import styles from './index.less';
import './index.less'

interface IProps {
  className?: any;
}

export default memo(function LoadingGracile(props: IProps) {
  const { className } = props;
  return <div className={classnames(styles.spinner, styles.center)}>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
    <div className={styles.spinnerBlade}></div>
  </div>

});
