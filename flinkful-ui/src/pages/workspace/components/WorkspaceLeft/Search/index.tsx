import { useMemo } from 'react';
import { Input, message } from 'antd';
import styles from '../index.less';

interface IProps {
  setSearchValue: React.Dispatch<React.SetStateAction<string>>;
  searchValue: string;
}

const DatabaseSearch: React.FC<IProps> = (props) => {
  const { setSearchValue, searchValue } = props;

  const getShortcut = useMemo(() => {
    // mac
    let str = '⌘ J';
    return <span className={styles.shortCut}>{str}</span>;
  }, []);

  return (
    <Input.Search
      className={styles.searchInput}
      placeholder={'搜索'}
      size="small"
      onChange={(e) => {
        setSearchValue(e.target.value);
      }}
      suffix={getShortcut}
      onSearch={() => {
        message.info('搜索：' + searchValue);
      }}
    />
  );
};

export default DatabaseSearch;
